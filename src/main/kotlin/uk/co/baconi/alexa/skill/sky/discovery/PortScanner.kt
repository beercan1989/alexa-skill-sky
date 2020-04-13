package uk.co.baconi.alexa.skill.sky.discovery

import com.github.maltalex.ineter.base.IPv4Address
import com.typesafe.config.Config
import org.slf4j.LoggerFactory
import java.io.IOException
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit.MILLISECONDS

class PortScanner(private val timeout: Int) {

    constructor(config: Config) : this(
        config.getDuration("discovery.timeout", MILLISECONDS).toInt()
    )

    companion object {
        private val logger = LoggerFactory.getLogger(PortScanner::class.java)
    }

    fun isPortOpen(address: IPv4Address, port: Int): Boolean = Socket().use { socket ->
        try {
            socket.reuseAddress = true
            socket.receiveBufferSize = 32
            socket.connect(InetSocketAddress(address.toInetAddress(), port), timeout)
            socket.setSoLinger(true, 0)
            socket.sendBufferSize = 16
            socket.tcpNoDelay = true

            socket.isConnected
        } catch (io: IOException) {
            logger.trace("Address {} and port {} got {} with {}", address, port, io::class.java.name, io.message)
            false
        } catch (exception: Exception) {
            logger.error("Unexpected: {} with {}", exception::class.java.name, exception.message)
            false
        }
    }
}