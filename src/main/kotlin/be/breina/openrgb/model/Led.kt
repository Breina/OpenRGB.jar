package be.breina.openrgb.model

import be.breina.openrgb.util.LittleEndianInputStream
import java.awt.Color
import java.util.function.Consumer

@Suppress("MemberVisibilityCanBePrivate")
class Led private constructor(
    val name: String?,
    val value: Int,
) {

    private lateinit var colorSetter: Consumer<Color>

    fun setColor(color: Color) {
        colorSetter.accept(color)
    }

    internal fun colorMutator(colorSetter: Consumer<Color>) {
        this.colorSetter = colorSetter
    }

    internal companion object {
        fun from(input: LittleEndianInputStream): Led {
            val name = input.readAscii()
            val value = input.readInt()

            return Led(name, value)
        }
    }
}