package be.breina.openrgb.model

import be.breina.openrgb.util.LittleEndianInputStream

@Suppress("MemberVisibilityCanBePrivate")
class Color(r: Int, g: Int, b: Int) {

    var red: Byte = r.toByte()
    var green: Byte = g.toByte()
    var blue: Byte = b.toByte()

    var color: java.awt.Color = java.awt.Color(red.toInt() and 0xFF, green.toInt() and 0xFF, blue.toInt() and 0xFF)
        set(value) {
            red = value.red.toByte()
            green = value.green.toByte()
            blue = value.blue.toByte()
        }

    internal companion object {
        internal fun from(input: LittleEndianInputStream): Color {
            val red = input.read()
            val green = input.read()
            val blue = input.read()
            input.skipNBytes(1)

            return Color(red, green, blue)
        }
    }
}