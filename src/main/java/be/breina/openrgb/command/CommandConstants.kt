package be.breina.openrgb.command

import java.nio.charset.StandardCharsets

object CommandConstants {
    const val SIZE = 16
    val COMMAND_HEADER = "ORGB".toByteArray(StandardCharsets.US_ASCII)
}