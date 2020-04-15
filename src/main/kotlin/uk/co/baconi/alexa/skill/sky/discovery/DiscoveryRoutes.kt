package uk.co.baconi.alexa.skill.sky.discovery

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import uk.co.baconi.alexa.skill.sky.health.HealthState.UP

object DiscoveryRoutes {

    private val logger = LoggerFactory.getLogger(DiscoveryRoutes::class.java)

    fun Application.discoveryRoutes(discoveryService: DiscoveryService) = routing {
        get("/discover") {

            val activeDevices = discoveryService
                .discoverDevicesAsync()
                .await()

            logger.debug("Found these active Sky devices: {}", activeDevices)

            call.respond(activeDevices)
        }
    }
}