package be.breina.openrgb.model;

import java.io.IOException;
import lombok.ToString;
import be.breina.openrgb.util.LittleEndianInputStream;

@ToString
public class Zone {

    private final String name;
    private final ZoneType type;
    private final int ledsMin, ledsMax, ledCount;
    private MatrixMap matrix;

    Zone(LittleEndianInputStream input) throws IOException {
        name = input.readAscii();
        type = ZoneType.values()[input.readInt()];
        ledsMin = input.readInt();
        ledsMax = input.readInt();
        ledCount = input.readInt();

        if (input.readUnsignedShort() > 0) {
            matrix = new MatrixMap(input);
        }
    }

    public String getName() {
        return name;
    }

    public ZoneType getType() {
        return type;
    }

    public int getLedsMin() {
        return ledsMin;
    }

    public int getLedsMax() {
        return ledsMax;
    }

    public int getLedCount() {
        return ledCount;
    }

    public MatrixMap getMatrix() {
        return matrix;
    }
}
