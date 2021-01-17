package be.breina.openrgb.model

import be.breina.openrgb.util.LittleEndianInputStream

@Suppress("MemberVisibilityCanBePrivate")
class Color(r: Byte, g: Byte, b: Byte) {

    var red: Byte = r
        internal set

    var green: Byte = g
        internal set

    var blue: Byte = b
        internal set

    internal companion object {
        internal fun from(input: LittleEndianInputStream): Color {
            val red = input.read().toByte()
            val green = input.read().toByte()
            val blue = input.read().toByte()
            input.skipNBytes(1)

            return Color(red, green, blue)
        }
    }
}