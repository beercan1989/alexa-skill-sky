package uk.co.baconi.alexa.skill.sky.health

import kotlinx.serialization.Serializable

@Serializable
data class HealthStatus(val status: HealthState)