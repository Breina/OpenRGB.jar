package be.breina.openrgb.command

import java.nio.charset.StandardCharsets

object CommandConstants {
    const val SIZE = 16
    val PREAMBLE = "ORGB".toByteArray(StandardCharsets.US_ASCII)
}