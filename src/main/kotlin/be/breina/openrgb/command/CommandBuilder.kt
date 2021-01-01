package be.breina.openrgb.command

import java.nio.ByteBuffer
import java.nio.ByteOrder

class CommandBuilder {
    private val byteBuffer: ByteBuffer = ByteBuffer
        .allocate(CommandConstants.SIZE)
        .order(ByteOrder.LITTLE_ENDIAN)
        .put(CommandConstants.COMMAND_HEADER)

    fun device(id: Int): CommandBuilder {
        byteBuffer.putInt(4, id)
        return this
    }

    fun command(commandId: CommandId): CommandBuilder {
        byteBuffer.putInt(8, commandId.id)
        return this
    }

    fun size(size: Int): CommandBuilder {
        byteBuffer.putInt(12, size)
        return this
    }

    fun build(): ByteArray = byteBuffer.array()

}