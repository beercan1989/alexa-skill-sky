@file:UseSerializers(IPv4AddressSerializer::class)

package uk.co.baconi.alexa.skill.sky.discovery

import com.github.maltalex.ineter.base.IPv4Address
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Device(val ip: IPv4Address, val remotePort: Int, val restPort: Int)
