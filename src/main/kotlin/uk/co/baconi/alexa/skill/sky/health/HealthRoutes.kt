package uk.co.baconi.alexa.skill.sky.health

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import uk.co.baconi.alexa.skill.sky.health.HealthState.UP

fun Application.healthRoutes() {
    routing {
        get("/health") {
            call.respond(HealthStatus(UP))
        }
    }
}