package be.breina.openrgb.model

import be.breina.openrgb.util.LittleEndianInputStream

class MatrixMap private constructor(val height: Int, val width: Int, val matrix: Array<IntArray>) {

    internal companion object {
        fun from(input: LittleEndianInputStream): MatrixMap? {
            if (input.readUnsignedShort() <= 0) {
                return null;
            }

            val height: Int = input.readInt()
            val width: Int = input.readInt()
            val matrix: Array<IntArray> = Array(height) { IntArray(width) { input.readInt() } }

            return MatrixMap(height, width, matrix)
        }
    }
}