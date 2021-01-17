package be.breina.openrgb;

import java.util.OptionalInt;

public class Request {

    private final int command;
    private final int deviceId;
    private final byte[] data;

    public Request(int command, Integer deviceId, byte[] data) {
        this.command = command;
        this.deviceId = deviceId;
        this.data = data;
    }

    public int getCommand() {
        return command;
    }

    public ByteChecker getData() {
        return ByteChecker.parse(data);
    }

    public int getDeviceId() {
        return deviceId;
    }
}
