package be.breina.openrgb.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import lombok.experimental.Delegate;
import be.breina.openrgb.util.LittleEndianInputStream;

public class Color {

    @Delegate
    private java.awt.Color color;

    public Color(java.awt.Color color) {
        this.color = color;
    }

    Color(LittleEndianInputStream input) throws IOException {
        final var red = input.read();
        final var green = input.read();
        final var blue = input.read();
        input.skipNBytes(1);

        color = new java.awt.Color(red, green, blue);
    }

    void setColor(java.awt.Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color.toString();
    }
}
