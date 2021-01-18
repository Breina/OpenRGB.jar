import be.breina.openrgb.OpenRgbClient;
import be.breina.openrgb.OpenRgbClientImpl;
import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class ControllerTest {

    /**
     * Yeah I know, this isn't unit testing. I just use it to quickly try stuff out.
     */

    private OpenRgbClient openRgbClient;

    @BeforeEach
    public void init() throws IOException, java.net.UnknownHostException {
        openRgbClient = new OpenRgbClientImpl();
    }

    @Test
    public void getDeviceCount() throws IOException {
        System.out.println(openRgbClient.getControllerCount());
    }

    @Test
    public void getDeviceData() {
        System.out.println(openRgbClient.getControllerData(0));
    }

    @Test
    public void updateLeds() {
        openRgbClient.updateLeds(0, Color.RED, Color.YELLOW);
    }

    @Test
    public void updateLedsWrapper() {
        final var device = openRgbClient.getControllerData(0);
        device.setColors(Color.RED, Color.RED, Color.RED, Color.RED, Color.RED);
    }

    @Test
    public void hueSweep() {
        final var deviceCount = openRgbClient.getControllerCount();

        for (int i = 0; i < 1000; i++) {
            final var targetColor = Color.getHSBColor((float) i / 1000f, 1f, 1f);

            for (int deviceIndex = 0; deviceIndex < deviceCount; deviceIndex++) {
                final var device = openRgbClient.getControllerData(deviceIndex);
                final var colorCount = device.getColors().length;

                final var newColors = new Color[colorCount];
                Arrays.fill(newColors, targetColor);
                openRgbClient.updateLeds(deviceIndex, newColors);
            }
        }
    }

    @Test
    public void saturationSweep() {
        final var deviceCount = openRgbClient.getControllerCount();

        for (int i = 0; i < 1000; i++) {
            final var targetColor = Color.getHSBColor(0.66f, (float) i / 1000f, 1f);

            for (int deviceIndex = 0; deviceIndex < deviceCount; deviceIndex++) {
                final var device = openRgbClient.getControllerData(deviceIndex);
                final var colorCount = device.getColors().length;

                final var newColors = new Color[colorCount];
                Arrays.fill(newColors, targetColor);
                openRgbClient.updateLeds(deviceIndex, newColors);
            }
        }
    }

    @Test
    public void brightnessSweep() {
        final var deviceCount = openRgbClient.getControllerCount();

        for (int i = 0; i < 1000; i++) {
            final var targetColor = Color.getHSBColor(0.33f, 1f, (float) i / 1000f);

            for (int deviceIndex = 0; deviceIndex < deviceCount; deviceIndex++) {
                final var device = openRgbClient.getControllerData(deviceIndex);
                final var colorCount = device.getColors().length;

                final var newColors = new Color[colorCount];
                Arrays.fill(newColors, targetColor);
                openRgbClient.updateLeds(deviceIndex, newColors);
            }
        }
    }

    @Test
    public void setAll() {
        final var deviceCount = openRgbClient.getControllerCount();

        for (int deviceIndex = 0; deviceIndex < deviceCount; deviceIndex++) {
            final var device = openRgbClient.getControllerData(deviceIndex);
            final var colorCount = device.getColors().length;

            final var newColors = new Color[colorCount];
            Arrays.fill(newColors, Color.BLUE);
            openRgbClient.updateLeds(deviceIndex, newColors);
        }
    }

    @Test
    public void setSingleLed() {
        openRgbClient.updateLed(0, 0, Color.GREEN);
    }

    @Test
    public void ledCountEqualToColorCount() {
        final var size = openRgbClient.getControllerCount();
        for (int i = 0; i < size; i++) {
            final var device = openRgbClient.getControllerData(i);
            System.out.print(device.getName() + ": ");
            Assertions.assertEquals(device.getColors().length, device.getLeds().length, device::getName);
        }
    }

    @Test
    public void setFirstLedGreenLastLedRed() {
        final var size = openRgbClient.getControllerCount();
        for (int deviceIndex = 0; deviceIndex < size; deviceIndex++) {
            final var device = openRgbClient.getControllerData(deviceIndex);
            final var newColors = new Color[device.getLeds().length];
            Arrays.fill(newColors, Color.BLACK);
            openRgbClient.updateLeds(deviceIndex, newColors);
        }

        for (int deviceIndex = 0; deviceIndex < size; deviceIndex++) {
            final var device = openRgbClient.getControllerData(deviceIndex);
            System.out.println(device.getName());

            final var leds = device.getLeds();

            leds[0].setColor(Color.GREEN);
            leds[leds.length - 1].setColor(Color.RED);
        }
    }

    @Test
    public void getAllDevices() {
        System.out.println(Arrays.toString(openRgbClient.getAllControllers()));
    }
}