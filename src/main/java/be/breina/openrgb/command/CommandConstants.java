package be.breina.openrgb.command;

import java.nio.charset.StandardCharsets;

public class CommandConstants {

    public static final int SIZE = 16;
    static final byte[] COMMAND_HEADER = "ORGB".getBytes(StandardCharsets.US_ASCII);

}
