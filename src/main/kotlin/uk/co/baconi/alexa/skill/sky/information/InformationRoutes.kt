package uk.co.baconi.alexa.skill.sky.information

import arrow.core.Either.Left
import arrow.core.Either.Right
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.routing
import org.slf4j.LoggerFactory

@KtorExperimentalLocationsAPI
object InformationRoutes {

    private val logger = LoggerFactory.getLogger(InformationRoutes::class.java)

    fun Application.informationRoutes(informationService: InformationService) = routing {
        get<InformationLocation> { location ->

            val result = informationService
                .getInformation(location.ip, location.port)
                .attempt()
                .suspended()

            when(result) {
                is Left<Throwable> -> {
                    logger.error("Error getting information.", result.a)
                    call.respond(HttpStatusCode.InternalServerError)
                }
                is Right<Map<String, String>> -> {
                    logger.debug("Found this information about devices: {}", result.b)
                    call.respond(result.b)
                }
            }
        }
    }
}