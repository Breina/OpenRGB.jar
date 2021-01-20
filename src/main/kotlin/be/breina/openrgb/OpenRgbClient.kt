package be.breina.openrgb

import be.breina.openrgb.model.Device
import java.awt.Color

interface OpenRgbClient {

    /**
     * Updates the name of this OpenRGB client to the OpenRGB server.
     * 
     * @param clientName The name of this OpenRGB client.
     */
    fun setName(clientName: String)
    val serverProtocolVersion: Int
    val controllerCount: Int
    fun getControllerData(deviceIndex: Int): Device?
    val allControllers: Array<Device>
    fun updateLed(deviceIndex: Int, ledIndex: Int, color: Color)
    fun updateLeds(deviceIndex: Int, vararg colors: Color)
    fun updateMode(deviceIndex: Int, modeIndex: Int)
}