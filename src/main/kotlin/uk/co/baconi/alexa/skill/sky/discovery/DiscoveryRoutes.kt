package uk.co.baconi.alexa.skill.sky.discovery

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import org.slf4j.LoggerFactory

object DiscoveryRoutes {

    private val logger = LoggerFactory.getLogger(DiscoveryRoutes::class.java)

    fun Application.discoveryRoutes(discoveryService: DiscoveryService) = routing {
        get("/discover") {

            val activeDevices = discoveryService
                .discoverDevices()

            logger.debug("Found these active Sky devices: {}", activeDevices)

            call.respond(activeDevices)
        }
    }
}