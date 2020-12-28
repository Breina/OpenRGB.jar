VERY EARLY VERSION! Prone to breaking changes
===

Java client to connect to an [OpenRGB](https://gitlab.com/CalcProgrammer1/OpenRGB) server.

Special thanks to the [C# implementation](https://github.com/diogotr7/OpenRGB.NET), which made this project a walk in the park.

```java
import be.breina.openrgb.OpenRgbClientImpl;
import be.breina.openrgb.OpenRgbClient;
import be.breina.openrgb.model.Device;
import java.awt.Color;

public class Example {

    public static void main(String[] args) {
        var client = new OpenRgbClientImpl();

        // Get the total devices
        int deviceCount = client.getControllerCount();

        // Get all devices
        Device[] devices = client.getAllControllers();

        // Get device of index 2
        Device device = client.getControllerData(2);
        
        // Device data
        device.getName();
        device.getDescription();
        device.getLocation();
        // ...

        // Set first 3 LEDs of this device
        device.setColors(Color.RED, Color.GREEN, new Color(0, 0, 255));
        // Set first 3 LEDs of device of index 2
        client.updateLeds(2, Color.RED, Color.GREEN, new Color(0, 0, 255));

        // Set single LED of this device at index 3
        device.getLeds()[3].setColor(Color.PINK);
        // or
        client.updateLed(2, 3, Color.PINK);
        
        // Set the name which will show up in OpenRGB
        client.setName("Custom client name");
        
        // Get OpenRGBs protocol version.
        client.getServerProtocolVersion();
    }
}
```

RGBController
---
- [x] Get device count
- [x] Get device data
- [x] Get protocol version
- [x] Set client name
- [x] Update LEDs
- [x] Update single LED
- [ ] Resize zone
- [ ] Update zone LEDs
- [ ] Set custom mode
- [ ] Update mode


