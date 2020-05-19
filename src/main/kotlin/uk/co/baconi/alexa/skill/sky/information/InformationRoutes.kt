@file:UseSerializers(IPv4AddressSerializer::class)

package uk.co.baconi.alexa.skill.sky.information

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.github.maltalex.ineter.base.IPv4Address
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.routing
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.slf4j.LoggerFactory
import uk.co.baconi.alexa.skill.sky.ip.IPv4AddressSerializer

@KtorExperimentalLocationsAPI
object InformationRoutes {

    private val logger = LoggerFactory.getLogger(InformationRoutes::class.java)

    @Serializable
    @Location("/information/{ip}/{port}")
    data class InformationLocation(val ip: IPv4Address, val port: Int)

    fun Application.informationRoutes(informationService: InformationService) = routing {
        get<InformationLocation> { request ->

            val result = informationService
                .getInformation(request.ip, request.port)
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