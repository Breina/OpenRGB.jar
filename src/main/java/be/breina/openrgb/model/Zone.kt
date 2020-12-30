package be.breina.openrgb.model

import be.breina.openrgb.model.enumeration.ZoneType
import be.breina.openrgb.util.LittleEndianInputStream
import lombok.ToString

@ToString
class Zone internal constructor(
    val name: String?,
    val type: ZoneType,
    val ledsMin: Int,
    val ledsMax: Int,
    val ledCount: Int,
    var matrix: MatrixMap? = null,
) {

    companion object {
        fun from(input: LittleEndianInputStream): Zone {
            val name = input.readAscii()
            val type = ZoneType.values()[input.readInt()]
            val ledsMin = input.readInt()
            val ledsMax = input.readInt()
            val ledCount = input.readInt()
            val matrix: MatrixMap? = MatrixMap.from(input)

            return Zone(name, type, ledsMin, ledsMax, ledCount, matrix)
        }
    }
}