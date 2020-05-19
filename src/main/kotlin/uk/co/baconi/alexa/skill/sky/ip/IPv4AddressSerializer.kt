package uk.co.baconi.alexa.skill.sky.ip

import com.github.maltalex.ineter.base.IPv4Address
import kotlinx.serialization.*

@Serializer(forClass = IPv4Address::class)
object IPv4AddressSerializer : KSerializer<IPv4Address> {

    override val descriptor: SerialDescriptor = PrimitiveDescriptor("IPv4Address", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: IPv4Address) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): IPv4Address {
        return IPv4Address.of(decoder.decodeString())
    }
}
