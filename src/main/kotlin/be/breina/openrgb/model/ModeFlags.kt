package be.breina.openrgb.model

interface ModeFlags {
    fun hasSpeed(): Boolean
    fun hasDirectionLR(): Boolean
    fun hasDirectionUP(): Boolean
    fun hasDirectionHv(): Boolean
    fun hasBrightness(): Boolean
    fun hasPerLedColor(): Boolean
    fun hasModeSpecificColor(): Boolean
    fun hasRandomColor(): Boolean
    fun hasDirection(): Boolean
}