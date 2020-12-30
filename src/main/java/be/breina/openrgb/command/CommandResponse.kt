package be.breina.openrgb.command

import be.breina.openrgb.command.CommandId
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
        check(Arrays.mismatch(CommandConstants.COMMAND_HEADER, byteBuffer.array()) == CommandConstants.COMMAND_HEADER.size) { "Wrong length!" }
        deviceId = byteBuffer.getInt(4)
        commandId = CommandId.Companion.from(byteBuffer.getInt(8))
        size = byteBuffer.getInt(12)
    }
}