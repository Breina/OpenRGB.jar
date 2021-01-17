package be.breina.openrgb;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class LittleEndianOutputStream implements DataOutput {

    private final OutputStream out;

    public LittleEndianOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    private static ByteBuffer byteBufferOfBytes(int i) {
        return ByteBuffer.allocate(i).order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        write(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) throws IOException {
        write(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        write(byteBufferOfBytes(2).putShort((short) v).array());
    }

    @Override
    public void writeChar(int v) throws IOException {
        write(byteBufferOfBytes(2).putChar((char) v).array());
    }

    @Override
    public void writeInt(int v) throws IOException {
        write(byteBufferOfBytes(4).putInt(v).array());
    }

    @Override
    public void writeLong(long v) throws IOException {
        write(byteBufferOfBytes(8).putLong(v).array());
    }

    @Override
    public void writeFloat(float v) throws IOException {
        write(byteBufferOfBytes(4).putFloat(v).array());
    }

    @Override
    public void writeDouble(double v) throws IOException {
        write(byteBufferOfBytes(8).putDouble(v).array());
    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        write(s.getBytes(StandardCharsets.US_ASCII));
    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {
        final var length = s.length();
        writeInt(length);
        write(s.getBytes(StandardCharsets.US_ASCII));
    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {
        final var length = s.length();
        writeInt(length);
        write(s.getBytes(StandardCharsets.UTF_8));
    }

    public void writeHeader(int command, int deviceId, int length) throws IOException {
        writeBytes("ORGB");
        writeInt(deviceId);
        writeInt(command);
        writeInt(length);
    }
}
