package be.breina.openrgb.model

class ModeFlagsImpl(private val flags: Int) : ModeFlags {

    override fun hasSpeed(): Boolean = flags and 1 != 0
    override fun hasDirectionLR(): Boolean = checkBit(1)
    override fun hasDirectionUP(): Boolean = checkBit(2)
    override fun hasDirectionHv(): Boolean = checkBit(3)
    override fun hasBrightness(): Boolean = checkBit(4)
    override fun hasPerLedColor(): Boolean = checkBit(5)
    override fun hasModeSpecificColor(): Boolean = checkBit(6)
    override fun hasRandomColor(): Boolean = checkBit(7)
    override fun hasDirection(): Boolean = flags and 0b0000_1110 != 0

    private fun checkBit(bit: Int): Boolean = flags and 1 shl bit != 0
}