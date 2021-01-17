package be.breina.openrgb;

import org.junit.jupiter.api.Assertions;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class ByteChecker {

    private static final String PREAMBLE = "ORGB";

    private final byte[] bytes;
    private int index;

    private ByteChecker(byte[] bytes) {
        this.bytes = bytes;
        this.index = 0;
    }

    public static ByteChecker parse(byte[] bytes) {
        return new ByteChecker(bytes);
    }

    public ByteChecker expectText(String expected) {
        final var length = expected.length();
        expectUnsignedShort(length);
        Assertions.assertEquals(expected, new String(readNextBytes(length).array(), StandardCharsets.US_ASCII));
        return this;
    }

    public ByteChecker ignoreText() {
        final var length = readNextBytes(2).getShort();
        index += length;
        return this;
    }

    public ByteChecker expectInt(int expected) {
        Assertions.assertEquals(expected, readNextBytes(4).getInt());
        return this;
    }

    public ByteChecker ignoreInt() {
        index += 4;
        return this;
    }

    public ByteChecker expectUnsignedShort(int expected) {
        Assertions.assertEquals((short) (expected & 0xFFFF), readNextBytes(2).getShort());
        return this;
    }

    public ByteChecker ignoreShort() {
        index += 2;
        return this;
    }

    public ByteChecker expectHeader(int command) {
        return expectHeader(command, -1);
    }

    public ByteChecker expectHeader(int command, int size) {
        return expectHeader(-1, command, size);
    }

    public ByteChecker expectHeader(int deviceId, int command, int size) {
        Assertions.assertEquals(PREAMBLE, new String(readNextBytes(4).array(), StandardCharsets.US_ASCII));

        // Duplicated code here specifically makes it more readable for what might have gone wrong.
        if (deviceId != -1) {
            expectInt(deviceId);
        } else {
            index += 4;
        }
        if (command != -1) {
            expectInt(command);
        } else {
            index += 4;
        }
        if (size != -1) {
            expectInt(size);
        } else {
            index += 4;
        }
        return this;
    }

    public void expectNoMoreBytes() {
        Assertions.assertEquals(index, bytes.length, () -> "There's " + (bytes.length - index) + " bytes left!");
    }

    private ByteBuffer readNextBytes(int size) {
        final var byteBuffer = ByteBuffer.wrap(bytes, index, size).order(LITTLE_ENDIAN);
        index += size;
        return byteBuffer;
    }
}
