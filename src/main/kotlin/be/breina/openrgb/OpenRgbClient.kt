package be.breina.openrgb

import be.breina.openrgb.model.Device
import java.awt.Color

interface OpenRgbClient {
    fun setName(clientName: String)
    val serverProtocolVersion: Int
    val controllerCount: Int
    fun getControllerData(deviceIndex: Int): Device?
    val allControllers: Array<Device>
    fun updateLed(deviceIndex: Int, ledIndex: Int, color: Color)
    fun updateLeds(deviceIndex: Int, vararg colors: Color)
}