package be.breina.openrgb.model;

import static lombok.AccessLevel.NONE;

import java.io.IOException;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Delegate;
import be.breina.openrgb.util.LittleEndianInputStream;

@ToString
@Getter
public class Mode {

    private final String name;
    private final int value;

    @Getter(NONE)
    @Delegate
    private final ModeFlags flags;

    private int speedMin, speedMax, speed;

    private int colorMin, colorMax;

    private Direction direction;

    private final ColorMode colorMode;
    private final Color[] colors;

    Mode(LittleEndianInputStream input) throws IOException {
        name = input.readAscii();
        value = input.readInt();
        flags = new ModeFlags(input.readInt());

        if (hasSpeed()) {
            speedMin = input.readInt();
            speedMax = input.readInt();
        } else {
            input.skipNBytes(8);
        }

        if (hasModeSpecificColor()) {
            colorMin = input.readInt();
            colorMax = input.readInt();
        } else {
            input.skipNBytes(8);
        }

        if (hasSpeed()) {
            speed = input.readInt();
        } else {
            input.skipNBytes(4);
        }

        if (hasDirection()) {
            direction = Direction.values()[input.readInt()];
        } else {
            input.skipNBytes(4);
        }

        colorMode = ColorMode.values()[input.readInt()];

        int colorCount = input.readUnsignedShort();
        colors = new Color[colorCount];
        for (int i = 0; i < colorCount; i++) {
            colors[i] = new Color(input);
        }
    }
}
