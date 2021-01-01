package be.breina.openrgb.util

import kotlin.Throws
import java.io.IOException
import java.nio.ByteOrder
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class LittleEndianInputStream(private val inputStream: InputStream) : InputStream() {

    @Throws(IOException::class)
    fun readInt(): Int = byteBufferOfBytes(4).int

    @Throws(IOException::class)
    fun readShort(): Short = byteBufferOfBytes(2).short

    @Throws(IOException::class)
    fun readUnsignedShort(): Int = byteBufferOfBytes(2).short.toInt() and 0xffff

    @Throws(IOException::class)
    fun readAscii(): String {
        val length = readUnsignedShort()
        return String(readNBytes(length), StandardCharsets.US_ASCII)
    }

    @Throws(IOException::class)
    private fun byteBufferOfBytes(i: Int): ByteBuffer = ByteBuffer.wrap(readNBytes(i)).order(ByteOrder.LITTLE_ENDIAN)

    @Throws(IOException::class)
    override fun read(): Int = inputStream.read()
}