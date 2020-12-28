package be.breina.openrgb.model;

import static lombok.AccessLevel.NONE;

import java.awt.Color;
import java.io.IOException;
import java.util.function.Consumer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import be.breina.openrgb.util.LittleEndianInputStream;

@EqualsAndHashCode
@ToString
@Getter
public class Led {

    private final String name;
    private final int value;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Getter(NONE)
    private Consumer<Color> colorSetter;

    Led(LittleEndianInputStream input) throws IOException {
        name = input.readAscii();
        value = input.readInt();
    }

    public void setColor(java.awt.Color color) {
        colorSetter.accept(color);
    }

    void colorMutator(Consumer<java.awt.Color> colorSetter) {
        this.colorSetter = colorSetter;
    }
}
