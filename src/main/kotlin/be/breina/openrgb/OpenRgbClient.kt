package be.breina.openrgb

import be.breina.openrgb.model.Device
import be.breina.openrgb.model.Led
import java.awt.Color

/**
 * The OpenRGB client which can be used to communicate to an OpenRGB server.
 */
interface OpenRgbClient {

    /**
     * Updates the name of this OpenRGB client to the OpenRGB server.
     * 
     * @param clientName The name of this OpenRGB client.
     */
    fun setName(clientName: String)

    /**
     * Returns the protocol version supported by the OpenRGB server.
     *
     * @return The server's supported protocol version.
     */
    val serverProtocolVersion: Int

    /**
     * Returns the amount of devices.
     *
     * @return The total amount of devices.
     */
    val deviceCount: Int

    /**
     * Returns a specific [Device].
     *
     * @param deviceIndex   The index of the [Device] to retrieve. Should be between 0 (inclusive) and [deviceCount]
     *                      (exclusive).
     *
     * @return If it exists, returns the [Device], otherwise `null`.
     */
    fun getDeviceData(deviceIndex: Int): Device?

    /**
     * Returns all devices.
     *
     * @return An array of all [Devices][Device].
     */
    val getAllDeviceData: Array<Device>

    /**
     * Updates a single [Led] with the given color.
     *
     * @param deviceIndex   The index of the [Device] to retrieve. Should be between 0 (inclusive) and [deviceCount]
     *                      (exclusive).
     * @param ledIndex      The index of the [Led] which needs to be updated. Should refer to a value within the bounds of
     *                      [Device.leds].
     * @param color         The new color to set the [Led] to.
     */
    fun updateLed(deviceIndex: Int, ledIndex: Int, color: Color)

    /**
     * Updates multiple LEDs with the given color.
     *
     * @param deviceIndex   The index of the [Device] to retrieve. Should be between 0 (inclusive) and [deviceCount]
     *                      (exclusive).
     * @param ledIndex      The index of the LED which needs to be updated. Should refer to a value within the bounds of
     *                      [Device.leds].
     * @param color         The new colors to be set. These will map to [Device.leds] in the order that they are given.
     */
    fun updateLeds(deviceIndex: Int, vararg colors: Color)

    /**
     * TODO
     */
    fun updateMode(deviceIndex: Int, modeIndex: Int)
}