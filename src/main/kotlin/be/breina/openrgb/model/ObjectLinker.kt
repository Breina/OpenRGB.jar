package be.breina.openrgb.model

import be.breina.openrgb.OpenRgbClient
import java.awt.Color

object ObjectLinker {
    fun link(device: Device?, deviceIndex: Int, client: OpenRgbClient) {
        device!!.colorMutator { colors: Array<Color> -> client.updateLeds(deviceIndex, *colors) }
        val leds = device.leds
        var i = 0
        val ledsLength = leds.size
        while (i < ledsLength) {
            val led = leds[i]
            val ledIndex = i
            led.colorMutator { color: Color ->
                client.updateLed(deviceIndex, ledIndex, color)
                val c = device.colors[ledIndex]
                c.red = color.red.toByte()
                c.green = color.green.toByte()
                c.blue = color.blue.toByte()
            }
            i++
        }
    }
}