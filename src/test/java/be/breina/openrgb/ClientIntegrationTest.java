package be.breina.openrgb;

import org.junit.jupiter.api.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ClientIntegrationTest {

    private static final int CLIENT_PROTOCOL_VERSION = 1;

    private static final int REQUEST_CONTROLLER_COUNT = 0;
    private static final int REQUEST_CONTROLLER_DATA = 1;
    private static final int REQUEST_PROTOCOL_VERSION = 40;
    private static final int SET_CLIENT_NAME = 50;
    private static final int RESIZE_ZONE = 1000;
    private static final int UPDATE_LEDS = 1050;
    private static final int UPDATE_ZONE_LEDS = 1051;
    private static final int UPDATE_SINGLE_LED = 1052;
    private static final int SET_CUSTOM_MODE = 1100;
    private static final int UPDATE_MODE = 1101;

    private DataInputStream inputStream;
    private LittleEndianOutputStream outputStream;

    private OpenRgbClient openRgbClient;
    private ServerSocket server;
    private CompletableFuture<OpenRgbClient> createClient;

    @BeforeEach
    public void init() throws IOException, ExecutionException, InterruptedException {
        server = new ServerSocket(6742);

        new CompletableFuture<OpenRgbClient>();

        createClient = CompletableFuture.supplyAsync(OpenRgbClientImpl::new);

        final var createServer = CompletableFuture.supplyAsync(
                () -> Assertions.assertDoesNotThrow(() -> server.accept())
        );

        final var socket = createServer.get();

        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new LittleEndianOutputStream(socket.getOutputStream());
    }

    @AfterEach
    public void shutDown() throws IOException {
        server.close();
    }

    @Test
    public void setupName() {
        final var request = nextRequest();

        Assertions.assertEquals(SET_CLIENT_NAME, request.getCommand());
    }

    @Test
    public void setupProtocolVersion1() throws IOException, ExecutionException, InterruptedException {
        Assumptions.assumeTrue(executesWithoutError(this::setupName));

        final var request = nextRequest();

        Assertions.assertEquals(REQUEST_PROTOCOL_VERSION, request.getCommand());

        final var data = request.getData();
        data.expectInt(CLIENT_PROTOCOL_VERSION);

        outputStream.writeHeader(REQUEST_PROTOCOL_VERSION, -1, 4);
        outputStream.writeInt(1);

        openRgbClient = createClient.get();
    }

    @Test
    public void getControllerCount() throws IOException, ExecutionException, InterruptedException {
        Assumptions.assumeTrue(executesWithoutError(this::setupProtocolVersion1));

        final var getControllerCount = CompletableFuture.supplyAsync(
                () -> openRgbClient.getDeviceCount()
        );

        final var request = nextRequest();

        Assertions.assertEquals(REQUEST_CONTROLLER_COUNT, request.getCommand());
        request.getData().expectNoMoreBytes();

        outputStream.writeHeader(REQUEST_CONTROLLER_COUNT, -1, 4);

        final var expectedControllerCount = 1337;
        outputStream.writeInt(expectedControllerCount);

        Assertions.assertEquals(expectedControllerCount, getControllerCount.get());
    }

    private Request nextRequest() {
        return Assertions.assertDoesNotThrow(() -> {
            final var preamble = new String(inputStream.readNBytes(4), StandardCharsets.US_ASCII);
            Assertions.assertEquals("ORGB", preamble);

            int deviceId = nextInt();
            int command = nextInt();
            int length = nextInt();

            return new Request(command, deviceId, inputStream.readNBytes(length));
        });
    }

    private int nextInt() {
        return Assertions.assertDoesNotThrow(() ->
                ByteBuffer.wrap(inputStream.readNBytes(4))
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .getInt()
        );
    }

    private boolean executesWithoutError(ThrowingRunnable execution) {
        try {
            execution.run();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @SuppressWarnings("ProhibitedExceptionDeclared")
    @FunctionalInterface
    private interface ThrowingRunnable extends Serializable {

        void run() throws Throwable;
    }
}
