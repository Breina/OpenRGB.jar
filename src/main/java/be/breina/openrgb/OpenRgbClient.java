package be.breina.openrgb;

import be.breina.openrgb.model.Device;

public interface OpenRgbClient {

    void setName(String clientName);

    int getServerProtocolVersion();

    int getControllerCount();

    Device getControllerData(int deviceIndex);

    void updateLed(int deviceIndex, int ledIndex, java.awt.Color color);

    void updateLeds(int deviceIndex, java.awt.Color... colors);
}
