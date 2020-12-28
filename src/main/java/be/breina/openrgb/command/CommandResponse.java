package be.breina.openrgb.command;

import static java.nio.ByteOrder.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class CommandResponse {

    private final int deviceId;
    private final CommandId commandId;
    private final int size;

    public static CommandResponse from(byte[] command) {
        return new CommandResponse(command);
    }

    private CommandResponse(byte[] command) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(command).order(LITTLE_ENDIAN);

        if (Arrays.mismatch(CommandConstants.COMMAND_HEADER, byteBuffer.array()) != CommandConstants.COMMAND_HEADER.length) {
            throw new IllegalStateException("Wrong length!");
        }

        deviceId = byteBuffer.getInt(4);
        commandId = CommandId.from(byteBuffer.getInt(8));
        size = byteBuffer.getInt(12);
    }

    public int getDeviceId() {
        return deviceId;
    }

    public CommandId getCommandId() {
        return commandId;
    }

    public int getSize() {
        return size;
    }
}
