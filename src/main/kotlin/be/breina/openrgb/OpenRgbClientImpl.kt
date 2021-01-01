package be.breina.openrgb

import be.breina.openrgb.command.CommandBuilder
import be.breina.openrgb.command.CommandConstants.SIZE
import be.breina.openrgb.command.CommandId
import be.breina.openrgb.command.CommandResponse
import be.breina.openrgb.model.Device
import be.breina.openrgb.model.ObjectFactory.device
import be.breina.openrgb.model.ObjectLinker.link
import java.awt.Color
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Consumer
import java.util.stream.IntStream

class OpenRgbClientImpl : OpenRgbClient {
    private val socket: Socket = Socket(IP, PORT)
    private val outputStream: OutputStream
    private val inputStream: InputStream
    private val protocolVersion: Int
    override fun setName(clientName: String) {
        sendMessage(
            { cb: CommandBuilder -> cb.command(CommandId.SET_CLIENT_NAME) },
            *(clientName + '\u0000').toByteArray(StandardCharsets.US_ASCII)
        )
    }

    override val serverProtocolVersion: Int
        get() {
            sendMessage({ cb: CommandBuilder -> cb.command(CommandId.REQUEST_PROTOCOL_VERSION) }, MAX_PROTOCOL_VERSION)
            return responseMessage().int
        }
    override val controllerCount: Int
        get() {
            sendMessage({ cb: CommandBuilder -> cb.command(CommandId.REQUEST_CONTROLLER_COUNT) })
            return responseMessage().int
        }

    override fun getControllerData(deviceIndex: Int): Device? {
        assert(deviceIndex >= 0)
        sendMessage({ cb: CommandBuilder -> cb.command(CommandId.REQUEST_CONTROLLER_DATA).device(deviceIndex) }, protocolVersion)
        val device = responseDevice()
        link(device, deviceIndex, this)
        return device
    }

    override val allControllers: Array<Device>
        get() = IntStream.range(0, controllerCount).mapToObj(::getControllerData).toArray(::arrayOfNulls)

    override fun updateLed(deviceIndex: Int, ledIndex: Int, color: Color) {
        updateLed(deviceIndex, ledIndex, be.breina.openrgb.model.Color(color.red, color.green, color.blue))
    }

    override fun updateLeds(deviceIndex: Int, vararg colors: Color) {
        updateLeds(
            deviceIndex,
            *Arrays.stream(colors).map { color -> be.breina.openrgb.model.Color(color.red, color.green, color.blue) }
                .toArray<be.breina.openrgb.model.Color>(::arrayOfNulls)
        )
    }

    private fun sendMessage(cb: Consumer<CommandBuilder>, data: Int) {
        sendMessage(
            cb,
            *ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data).array()
        )
    }

    private fun sendMessage(cb: Consumer<CommandBuilder>, vararg data: Byte) {
        val commandBuilder = CommandBuilder()
            .size(data.size)
        cb.accept(commandBuilder)
        val commandHeader = commandBuilder.build()
        try {
            outputStream.write(commandHeader)
            if (data.isNotEmpty()) {
                outputStream.write(data)
            }
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readMessage(): ByteArray {
        try {
            val responseHeader = CommandResponse.from(inputStream.readNBytes(SIZE))
            return inputStream.readNBytes(responseHeader.size)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ByteArray(0)
    }

    private fun updateLed(deviceIndex: Int, ledIndex: Int, color: be.breina.openrgb.model.Color) {
        assert(deviceIndex >= 0)
        val request = ByteBuffer.allocate(8)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(ledIndex)
            .put(color.red)
            .put(color.green)
            .put(color.blue)
        request.position(request.position() + 1)
        sendMessage({ cb: CommandBuilder -> cb.command(CommandId.UPDATE_SINGLE_LED).device(deviceIndex) }, *request.array())
    }

    private fun updateLeds(deviceIndex: Int, vararg colors: be.breina.openrgb.model.Color) {
        assert(deviceIndex >= 0)
        val request = ByteBuffer.allocate(4 + 2 + 4 * colors.size)
            .order(ByteOrder.LITTLE_ENDIAN)
            .position(4)
            .putShort(colors.size.toShort())
        Arrays.stream(colors).forEach { color: be.breina.openrgb.model.Color ->
            request
                .put(color.red)
                .put(color.green)
                .put(color.blue)
                .position(request.position() + 1)
        }
        sendMessage({ cb: CommandBuilder -> cb.command(CommandId.UPDATE_LEDS).device(deviceIndex) }, *request.array())
    }

    private fun responseMessage(): ByteBuffer = ByteBuffer.wrap(readMessage()).order(ByteOrder.LITTLE_ENDIAN)

    private fun responseDevice(): Device? {
        try {
            inputStream.skipNBytes(SIZE.toLong())
            return device(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private const val IP = "127.0.0.1"
        private const val PORT = 6742
        private const val NAME = "OpenRGB.jar"
        private const val MAX_PROTOCOL_VERSION = 1
    }

    init {
        outputStream = BufferedOutputStream(socket.getOutputStream(), SIZE)
        inputStream = BufferedInputStream(socket.getInputStream(), SIZE)
        setName(NAME)
        protocolVersion = serverProtocolVersion
    }
}