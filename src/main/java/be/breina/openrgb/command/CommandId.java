package be.breina.openrgb.command;

import java.util.Arrays;

public enum CommandId {

    REQUEST_CONTROLLER_COUNT(0),
    REQUEST_CONTROLLER_DATA(1),
    REQUEST_PROTOCOL_VERSION(40),
    SET_CLIENT_NAME(50),
    RESIZE_ZONE(1000),
    UPDATE_LEDS(1050),
    UPDATE_ZONE_LEDS(1051),
    UPDATE_SINGLE_LED(1052),
    SET_CUSTOM_MODE(1100),
    UPDATE_MODE(1101);

    CommandId(int id) {
        this.id = id;
    }

    private final int id;

    public int getId() {
        return id;
    }

    public static CommandId from(int id) {
        return Arrays.stream(values())
            .filter(commandId -> commandId.getId() == id)
            .findAny()
            .orElseThrow();
    }
}
