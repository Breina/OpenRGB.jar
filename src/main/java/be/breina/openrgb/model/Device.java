package be.breina.openrgb.model;

import static lombok.AccessLevel.NONE;

import java.io.IOException;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.ToString;
import be.breina.openrgb.util.Constants;
import be.breina.openrgb.util.LittleEndianInputStream;

@ToString
@Getter
public class Device {

    private final DeviceType type;

    private final String name;
    private String vendor;
    private final String description, version, serial, location;

    @Getter(NONE)
    private final int activeModeIndex;
    private final Mode[] modes;

    private final Zone[] zones;
    private final Led[] leds;
    private final Color[] colors;

    @Getter(NONE)
    private Consumer<java.awt.Color[]> colorSetter;

    public Mode getActiveMode() {
        return modes[activeModeIndex];
    }

    Device(LittleEndianInputStream input) throws IOException {
        final var duplicatePacketLength = input.readInt();
        type = DeviceType.values()[input.readInt()];
        name = input.readAscii();
        if (Constants.serverProtocol >= 1) {
            vendor = input.readAscii();
        }
        description = input.readAscii();
        version = input.readAscii();
        serial = input.readAscii();
        location = input.readAscii();

        final var modeCount = input.readUnsignedShort();
        modes = new Mode[modeCount];
        activeModeIndex = input.readInt();
        for (int i = 0; i < modeCount; i++) {
            modes[i] = new Mode(input);
        }

        final var zoneCount = input.readUnsignedShort();
        zones = new Zone[zoneCount];
        for (int i = 0; i < zoneCount; i++) {
            zones[i] = new Zone(input);
        }

        final var ledCount = input.readUnsignedShort();
        leds = new Led[ledCount];
        for (int i = 0; i < ledCount; i++) {
            leds[i] = new Led(input);
        }

        final var colorCount = input.readUnsignedShort();
        colors = new Color[colorCount];
        for (int i = 0; i < colorCount; i++) {
            colors[i] = new Color(input);
        }
    }

    public void setColors(java.awt.Color... colors) {
        assert colors.length <= this.colors.length;
        colorSetter.accept(colors);
    }

    void colorMutator(Consumer<java.awt.Color[]> colorSetter) {
        this.colorSetter = colorSetter;
    }
}
