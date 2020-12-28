package be.breina.openrgb.model;

public class ModeFlags {

    private final int flags;

    public ModeFlags(int flags) {
        this.flags = flags;
    }

    public boolean hasSpeed() {
        return (flags & 1) != 0;
    }

    public boolean hasDirectionLR() {
        return checkBit(1);
    }

    public boolean hasDirectionUP() {
        return checkBit(2);
    }

    public boolean hasDirectionHv() {
        return checkBit(3);
    }

    public boolean hasBrightness() {
        return checkBit(4);
    }

    public boolean hasPerLedColor() {
        return checkBit(5);
    }

    public boolean hasModeSpecificColor() {
        return checkBit(6);
    }

    public boolean hasRandomColor() {
        return checkBit(7);
    }

    public boolean hasDirection() {
        return (flags & 0b0000_1110) != 0;
    }

    private boolean checkBit(int bit) {
        return (flags & 0b1 << bit) != 0;
    }
}
