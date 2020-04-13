package uk.co.baconi.alexa.skill.sky.discovery

import com.github.maltalex.ineter.base.IPv4Address
import com.github.maltalex.ineter.range.IPv4Range
import com.typesafe.config.Config
import org.slf4j.LoggerFactory
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinPool
import java.util.stream.Stream
import kotlin.streams.toList

class DiscoveryService(private val network: String, private val portScanner: PortScanner) {

    constructor(config: Config, portScanner: PortScanner) : this(
        config.getString("discovery.network"),
        portScanner
    )

    companion object {
        private val logger = LoggerFactory.getLogger(DiscoveryService::class.java)
    }

    // TODO - Convert to a more Kotlin way of processing in parallel
    fun discoverActiveDevices(): List<Device> {

        return measureTimeMillis({ time -> logger.debug("Discovery took: {} ms", time) }) {

            val iPv4Range = IPv4Range.parse(network).toList()

            // Enables the parallel stream to run simultaneously for each ip address
            ForkJoinPool(iPv4Range.size).submit(Callable {

                iPv4Range
                    .parallelStream()
                    .flatMap { iPv4Address ->
                        logger.trace("Scanning {}", iPv4Address)
                        if (iPv4Address.hasRestInterfacePortOpen()) {
                            when {
                                iPv4Address.hasRemotePortOpen() -> {
                                    logger.trace("Found a possible SkyQ box at {}", iPv4Address)
                                    Stream.of(SkyQ(iPv4Address))
                                }
                                iPv4Address.hasLegacyRemotePortOpen() -> {
                                    logger.trace("Found a possible SkyHD box at {}", iPv4Address)
                                    Stream.of(SkyHd(iPv4Address))
                                }
                                else -> {
                                    logger.trace("Found a device with just a rest interface at {}", iPv4Address)
                                    Stream.empty()
                                }
                            }
                        } else {
                            logger.trace("Didn't find anything of note at {}", iPv4Address)
                            Stream.empty()
                        }
                    }
                    .toList()
            }).get()
        }
    }

    private fun IPv4Address.hasRestInterfacePortOpen() = portScanner.isPortOpen(this, 9006)
    private fun IPv4Address.hasRemotePortOpen() = portScanner.isPortOpen(this, 49160)
    private fun IPv4Address.hasLegacyRemotePortOpen() = portScanner.isPortOpen(this, 5900)

    private inline fun <T> measureTimeMillis(loggingFunction: (Long) -> Unit, function: () -> T): T {
        val startTime = System.currentTimeMillis()
        val result: T = function.invoke()
        loggingFunction.invoke(System.currentTimeMillis() - startTime)
        return result
    }
}