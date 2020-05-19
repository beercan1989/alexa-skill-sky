package uk.co.baconi.alexa.skill.sky

import com.github.maltalex.ineter.base.IPv4Address
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.CacheControl
import io.ktor.http.content.CachingOptions
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import uk.co.baconi.alexa.skill.sky.discovery.DiscoveryRoutes.discoveryRoutes
import uk.co.baconi.alexa.skill.sky.discovery.DiscoveryService
import uk.co.baconi.alexa.skill.sky.discovery.PortScanner
import uk.co.baconi.alexa.skill.sky.health.HealthRoutes.healthRoutes
import uk.co.baconi.alexa.skill.sky.information.InformationRoutes.informationRoutes
import uk.co.baconi.alexa.skill.sky.information.InformationService
import uk.co.baconi.alexa.skill.sky.ip.IPv4AddressConversionService

@Suppress("unused") // Inform the IDE that we are actually using this
@KtorExperimentalLocationsAPI
fun Application.main() {

    install(Locations)
    install(AutoHeadResponse)
    install(DataConversion) {
        convert(IPv4Address::class, IPv4AddressConversionService)
    }

    install(HSTS) {
        includeSubDomains = true
    }

    install(ContentNegotiation) {
        json()
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    // Disable caching via headers on all requests
    install(CachingHeaders) {
        // no-store
        options { CachingOptions(CacheControl.NoStore(null)) }
        // no-cache
        options { CachingOptions(CacheControl.NoCache(null)) }
        // must-revalidate, proxy-revalidate, max-age=0
        options { CachingOptions(CacheControl.MaxAge(0, mustRevalidate = true, proxyRevalidate = true)) }
    }

    // Load application configuration
    val configuration = ConfigFactory.load().getConfig("uk.co.baconi.alexa.skill.sky")

    // Create services
    val portScanner = PortScanner(configuration)
    val discoveryService = DiscoveryService(configuration, portScanner)
    val informationService = InformationService()

    // Add routing
    healthRoutes()
    discoveryRoutes(discoveryService)
    informationRoutes(informationService)
}