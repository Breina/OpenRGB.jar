package be.breina.openrgb.model

import be.breina.openrgb.model.enumeration.ColorMode
import be.breina.openrgb.model.enumeration.Direction
import be.breina.openrgb.util.LittleEndianInputStream

@Suppress("MemberVisibilityCanBePrivate")
class Mode private constructor(
        val name: String,
        val value: Int,
        private val flags: ModeFlags,
        val colorMode: ColorMode,
        val colors: Array<Color>,

        var speedMin: Int? = null,
        var speedMax: Int? = null,
        var speed: Int? = null,
        var colorMin: Int? = null,
        var colorMax: Int? = null,
        var direction: Direction? = null,
) : ModeFlags by flags {

    internal companion object {
        fun from(input: LittleEndianInputStream): Mode {
            val name = input.readAscii()
            val value = input.readInt()
            val flags = ModeFlagsImpl(input.readInt())

            val speedMin: Int?
            val speedMax: Int?
            if (flags.hasSpeed()) {
                speedMin = input.readInt()
                speedMax = input.readInt()
            } else {
                speedMin = null
                speedMax = null
                input.skipNBytes(8)
            }

            val colorMin: Int?
            val colorMax: Int?
            if (flags.hasModeSpecificColor()) {
                colorMin = input.readInt()
                colorMax = input.readInt()
            } else {
                colorMin = null
                colorMax = null
                input.skipNBytes(8)
            }

            val speed: Int?
            if (flags.hasSpeed()) {
                speed = input.readInt()
            } else {
                speed = null
                input.skipNBytes(4)
            }

            val direction: Direction?
            if (flags.hasDirection()) {
                direction = Direction.values()[input.readInt()]
            } else {
                direction = null
                input.skipNBytes(4)
            }

            val colorMode = ColorMode.values()[input.readInt()]
            val colorCount = input.readUnsignedShort()

            val colors = Array(colorCount) { Color.from(input) }

            return Mode(name, value, flags, colorMode, colors, speedMin, speedMax, speed, colorMin, colorMax, direction)
        }
    }
}