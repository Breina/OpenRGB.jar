package be.breina.openrgb.command;

import static be.breina.openrgb.command.CommandConstants.COMMAND_HEADER;
import static be.breina.openrgb.command.CommandConstants.SIZE;
import static java.nio.ByteOrder.*;

import java.nio.ByteBuffer;

public class CommandBuilder {

    private final ByteBuffer byteBuffer;

    public CommandBuilder() {
        byteBuffer = ByteBuffer.allocate(SIZE).order(LITTLE_ENDIAN).put(COMMAND_HEADER);
    }

    public CommandBuilder device(int id) {
        byteBuffer.putInt(4, id);
        return this;
    }

    public CommandBuilder command(CommandId commandId) {
        byteBuffer.putInt(8, commandId.getId());
        return this;
    }

    public CommandBuilder size(int size) {
        byteBuffer.putInt(12, size);
        return this;
    }

    public byte[] build() {
        return byteBuffer.array();
    }
}
