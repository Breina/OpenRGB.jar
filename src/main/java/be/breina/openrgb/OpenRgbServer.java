package be.breina.openrgb;

import static be.breina.openrgb.command.CommandConstants.SIZE;

import be.breina.openrgb.command.CommandResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class OpenRgbServer {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 6742;
    private static final String NAME = "OpenRGB.jar";
    private static final int PROTOCOL_VERSION = 1;

    private final Socket socket;
    private final DataOutputStream outputStream;
    private final InputStream inputStream;

    public static void main(String[] args) throws IOException, UnknownHostException {
        new OpenRgbServer();
    }

    public OpenRgbServer() throws IOException, UnknownHostException {
        final var server = new ServerSocket(PORT);

        socket = server.accept();

        outputStream = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream())/*, SIZE*/);
        inputStream = new BufferedInputStream(this.socket.getInputStream()/*, SIZE*/);

//        final var bytes = readMessage();

        System.out.println("Input:");
        for (;;) {
            System.out.println(inputStream.read());
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
}
