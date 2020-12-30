package be.breina.openrgb.model

import be.breina.openrgb.model.enumeration.DeviceType
import be.breina.openrgb.util.Constants
import be.breina.openrgb.util.LittleEndianInputStream
import lombok.EqualsAndHashCode
import java.util.function.Consumer

@Suppress("MemberVisibilityCanBePrivate")
class Device private constructor(
    val type: DeviceType,
    val name: String,
    var vendor: String?,
    val description: String,
    val version: String,
    val serial: String,
    val location: String,

    private val activeModeIndex: Int,

    val modes: Array<Mode>,
    val zones: Array<Zone>,
    val leds: Array<Led>,
    val colors: Array<Color>,
) {

    val activeMode: Mode get() = modes[activeModeIndex]

    @EqualsAndHashCode.Exclude
    private var colorSetter: Consumer<Array<java.awt.Color>>? = null

    fun setColors(vararg colors: java.awt.Color) {
        assert(colors.size <= this.colors.size)
        colorSetter!!.accept(arrayOf(*colors))
    }

    internal fun colorMutator(colorSetter: Consumer<Array<java.awt.Color>>?) {
        this.colorSetter = colorSetter
    }

    internal companion object {
        fun from(input: LittleEndianInputStream): Device {
            val duplicatePacketLength = input.readInt()
            val type = DeviceType.values()[input.readInt()]
            val name = input.readAscii()

            val vendor: String? =
                if (Constants.serverProtocol >= 1) {
                    input.readAscii()
                } else {
                    null
                }

            val description = input.readAscii()
            val version = input.readAscii()
            val serial = input.readAscii()
            val location = input.readAscii()

            val modeCount = input.readUnsignedShort()
            val activeModeIndex = input.readInt()
            val modes: Array<Mode> = Array(modeCount) { Mode.from(input) }

            val zoneCount = input.readUnsignedShort()
            val zones: Array<Zone> = Array(zoneCount) { Zone.from(input) }

            val ledCount = input.readUnsignedShort()
            val leds: Array<Led> = Array(ledCount) { Led.from(input) }

            val colorCount = input.readUnsignedShort()
            val colors: Array<Color> = Array(colorCount) { Color.from(input) }

            return Device(type, name, vendor, description, version, serial, location, activeModeIndex, modes, zones, leds, colors)
        }
    }
}