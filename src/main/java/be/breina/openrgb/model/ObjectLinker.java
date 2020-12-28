package be.breina.openrgb.model;

import be.breina.openrgb.OpenRgbClient;

public final class ObjectLinker {

    public static void link(Device device, int deviceIndex, OpenRgbClient client) {
        device.colorMutator(colors -> client.updateLeds(deviceIndex, colors));

        Led[] leds = device.getLeds();
        for (int i = 0, ledsLength = leds.length; i < ledsLength; i++) {
            Led led = leds[i];

            final int ledIndex = i;
            led.colorMutator(color -> {
                client.updateLed(deviceIndex, ledIndex, color);
                device.getColors()[ledIndex].setColor(color);
            });

        }
    }
}
