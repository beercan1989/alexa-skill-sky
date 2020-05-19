@file:UseSerializers(IPv4AddressSerializer::class)

package uk.co.baconi.alexa.skill.sky.information

import com.github.maltalex.ineter.base.IPv4Address
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import uk.co.baconi.alexa.skill.sky.ip.IPv4AddressSerializer

@Serializable
@KtorExperimentalLocationsAPI
@Location("/information/{ip}/{port}")
data class InformationLocation(val ip: IPv4Address, val port: Int)