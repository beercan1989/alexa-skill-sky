package uk.co.baconi.alexa.skill.sky.discovery

import com.github.maltalex.ineter.base.IPv4Address
import com.github.maltalex.ineter.range.IPv4Range
import com.typesafe.config.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

class DiscoveryService(private val iPv4Range: IPv4Range, private val portScanner: PortScanner) {

    private val coroutineDispatcher = Executors.newFixedThreadPool(iPv4Range.intLength()).asCoroutineDispatcher()

    constructor(config: Config, portScanner: PortScanner) : this(
        IPv4Range.parse(config.getString("discovery.network")),
        portScanner
    )

    companion object {
        private val logger = LoggerFactory.getLogger(DiscoveryService::class.java)
    }

    suspend fun discoverDevices(): List<Device> = withContext(Dispatchers.IO) {
        iPv4Range.map { iPv4Address ->
            async(coroutineDispatcher) {
                scanAddress(iPv4Address)
            }
        }.mapNotNull { deferred ->
            deferred.await()
        }
    }

    fun scanAddress(iPv4Address: IPv4Address): Device? {
        logger.trace("Scanning {}", iPv4Address)
        return when {
            // hasRestInterfacePortOpen
            portScanner.isPortOpen(iPv4Address, 9006) -> {
                when {
                    // hasRemotePortOpen
                    portScanner.isPortOpen(iPv4Address, 49160) -> {
                        logger.debug("Found a possible SkyQ box at {}", iPv4Address)
                        Device(iPv4Address, remotePort = 49160, restPort = 9006)
                    }
                    // hasLegacyRemotePortOpen
                    portScanner.isPortOpen(iPv4Address, 5900) -> {
                        logger.debug("Found a possible SkyHD box at {}", iPv4Address)
                        Device(iPv4Address, remotePort = 5900, restPort = 9006)
                    }
                    else -> {
                        logger.debug("Found a device with just a rest interface at {}", iPv4Address)
                        null
                    }
                }
            }
            else -> {
                logger.trace("Didn't find anything of note at {}", iPv4Address)
                null
            }
        }
    }
}