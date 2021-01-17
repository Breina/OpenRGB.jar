package be.breina.openrgb.command

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class CommandResponse private constructor(command: ByteArray) {
    val deviceId: Int
    val commandId: CommandId
    val size: Int

    companion object {
        fun from(command: ByteArray): CommandResponse = CommandResponse(command)
    }

    init {
        val byteBuffer = ByteBuffer.wrap(command).order(ByteOrder.LITTLE_ENDIAN)
        check(
            Arrays.mismatch(CommandConstants.PREAMBLE, byteBuffer.array()) == CommandConstants.PREAMBLE.size
        ) { "Wrong length!" }
        deviceId = byteBuffer.getInt(4)
        commandId = CommandId.Companion.from(byteBuffer.getInt(8))
        size = byteBuffer.getInt(12)
    }
}