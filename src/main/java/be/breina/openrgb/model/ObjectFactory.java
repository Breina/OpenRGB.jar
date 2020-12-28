package be.breina.openrgb.model;

import be.breina.openrgb.util.LittleEndianInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ObjectFactory {

    public static Device device(InputStream inputStream) {
        try (var input = new LittleEndianInputStream(inputStream)) {
            return new Device(input);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
