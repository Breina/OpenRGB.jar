package be.breina.openrgb.model

import be.breina.openrgb.util.LittleEndianInputStream
import java.io.IOException
import java.io.InputStream

object ObjectFactory {
    fun device(inputStream: InputStream): Device? {
        try {
            LittleEndianInputStream(inputStream).use { input -> return Device.from(input) }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}