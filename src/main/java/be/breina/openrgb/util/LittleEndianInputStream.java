package be.breina.openrgb.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public final class LittleEndianInputStream extends InputStream {

    private final InputStream inputStream;

    public LittleEndianInputStream(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
    }

    public int readInt() throws IOException {
        return byteBufferOfBytes(4).getInt();
    }

    public short readShort() throws IOException {
        return byteBufferOfBytes(2).getShort();
    }

    public int readUnsignedShort() throws IOException {
        return byteBufferOfBytes(2).getShort() & 0xffff;
    }

    public String readAscii() throws IOException {
        final var length = readUnsignedShort();
        return new String(readNBytes(length), StandardCharsets.US_ASCII);
    }

    private ByteBuffer byteBufferOfBytes(int i) throws IOException {
        return ByteBuffer.wrap(readNBytes(i)).order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
