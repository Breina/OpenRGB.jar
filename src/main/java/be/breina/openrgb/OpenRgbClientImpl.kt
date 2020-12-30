package be.breina.openrgb;

import static be.breina.openrgb.command.CommandConstants.SIZE;
import static be.breina.openrgb.command.CommandId.REQUEST_CONTROLLER_COUNT;
import static be.breina.openrgb.command.CommandId.REQUEST_CONTROLLER_DATA;
import static be.breina.openrgb.command.CommandId.REQUEST_PROTOCOL_VERSION;
import static be.breina.openrgb.command.CommandId.SET_CLIENT_NAME;
import static be.breina.openrgb.command.CommandId.UPDATE_LEDS;
import static be.breina.openrgb.command.CommandId.UPDATE_SINGLE_LED;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

import be.breina.openrgb.command.CommandBuilder;
import be.breina.openrgb.command.CommandResponse;
import be.breina.openrgb.model.Color;
import be.breina.openrgb.model.Device;
import be.breina.openrgb.model.ObjectFactory;
import be.breina.openrgb.model.ObjectLinker;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import lombok.NonNull;

public class OpenRgbClientImpl implements OpenRgbClient {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 6742;
    private static final String NAME = "OpenRGB.jar";
    private static final int MAX_PROTOCOL_VERSION = 1;

    private final Socket socket;
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final int protocolVersion;

    public OpenRgbClientImpl() throws IOException, UnknownHostException {
        socket = new Socket(IP, PORT);

        outputStream = new BufferedOutputStream(socket.getOutputStream(), SIZE);
        inputStream = new BufferedInputStream(socket.getInputStream(), SIZE);

        setName(NAME);
        protocolVersion = getServerProtocolVersion();
    }

    @Override
    public void setName(String clientName) {
        sendMessage(
            cb -> cb.command(SET_CLIENT_NAME),
            (clientName + '\0').getBytes(StandardCharsets.US_ASCII)
        );
    }

    @Override
    public int getServerProtocolVersion() {
        sendMessage(cb -> cb.command(REQUEST_PROTOCOL_VERSION), MAX_PROTOCOL_VERSION);
        return responseMessage().getInt();
    }

    @Override
    public int getControllerCount() {
        sendMessage(cb -> cb.command(REQUEST_CONTROLLER_COUNT));
        return responseMessage().getInt();
    }

    @Override
    public Device getControllerData(int deviceIndex) {
        assert deviceIndex >= 0;

        sendMessage(cb -> cb.command(REQUEST_CONTROLLER_DATA).device(deviceIndex), protocolVersion);

        final var device = responseDevice();
        ObjectLinker.link(device, deviceIndex, this);
        return device;
    }

    @Override
    public Device[] getAllControllers() {
        return IntStream.range(0, getControllerCount()).mapToObj(this::getControllerData).toArray(Device[]::new);
    }

    @Override
    public void updateLed(int deviceIndex, int ledIndex, java.awt.Color color) {
        updateLed(deviceIndex, ledIndex, new Color(color));
    }

    @Override
    public void updateLeds(int deviceIndex, java.awt.Color... colors) {
        updateLeds(
            deviceIndex, Arrays.stream(colors).map(Color::new).toArray(Color[]::new)
        );
    }

    private void sendMessage(Consumer<CommandBuilder> cb, int data) {
        sendMessage(cb,
            ByteBuffer.allocate(4).order(LITTLE_ENDIAN).putInt(data).array()
        );
    }

    private void sendMessage(Consumer<CommandBuilder> cb, byte... data) {
        final var commandBuilder = new CommandBuilder()
            .size(data.length);
        cb.accept(commandBuilder);
        final var commandHeader = commandBuilder.build();

        try {
            outputStream.write(commandHeader);

            if (data.length != 0) {
                outputStream.write(data);
            }

            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readMessage() {
        try {
            final var responseHeader = CommandResponse.from(inputStream.readNBytes(SIZE));
            return inputStream.readNBytes(responseHeader.getSize());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private void updateLed(int deviceIndex, int ledIndex, Color color) {
        assert deviceIndex >= 0;

        final var request = ByteBuffer.allocate(8)
            .order(LITTLE_ENDIAN)
            .putInt(ledIndex)
            .put((byte) color.getRed())
            .put((byte) color.getGreen())
            .put((byte) color.getBlue());

        request.position(request.position() + 1);

        sendMessage(cb -> cb.command(UPDATE_SINGLE_LED).device(deviceIndex), request.array());
    }

    private void updateLeds(int deviceIndex, @NonNull Color... colors) {
        assert deviceIndex >= 0;

        final var request = ByteBuffer.allocate(4 + 2 + 4 * colors.length)
            .order(LITTLE_ENDIAN)
            .position(4)
            .putShort((short) colors.length);

        Arrays.stream(colors).forEach(color -> request
            .put((byte) color.getRed())
            .put((byte) color.getGreen())
            .put((byte) color.getBlue())
            .position(request.position() + 1)
        );

        sendMessage(cb -> cb.command(UPDATE_LEDS).device(deviceIndex), request.array());
    }

    private ByteBuffer responseMessage() {
        return ByteBuffer.wrap(readMessage()).order(LITTLE_ENDIAN);
    }

    private Device responseDevice() {
        try {
            inputStream.skipNBytes(SIZE);
            return ObjectFactory.device(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
