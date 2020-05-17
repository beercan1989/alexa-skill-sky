package uk.co.baconi.alexa.skill.sky.discovery

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import org.slf4j.LoggerFactory

object DiscoveryRoutes {

    private val logger = LoggerFactory.getLogger(DiscoveryRoutes::class.java)

    fun Application.discoveryRoutes(discoveryService: DiscoveryService) = routing {
        get("/discover") {

            val result = discoveryService
                .discoverDevices()
                .attempt()
                .suspended()

            when(result) {
                is Left<Throwable> -> {
                    logger.error("Error discovering devices.", result.a)
                    call.respond(InternalServerError)
                }
                is Right<List<Device>> -> {
                    logger.debug("Found these active Sky devices: {}", result.b)
                    call.respond(result.b)
                }
            }
        }
    }
}