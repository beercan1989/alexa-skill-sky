package uk.co.baconi.alexa.skill.sky.ip

import com.github.maltalex.ineter.base.IPv4Address
import io.ktor.util.ConversionService
import io.ktor.util.DataConversionException
import java.lang.reflect.Type

object IPv4AddressConversionService : ConversionService {

    override fun fromValues(values: List<String>, type: Type): Any? {
        return values.singleOrNull()?.let { IPv4Address.of(it) }
    }

    override fun toValues(value: Any?): List<String> {
        return when (value) {
            null -> listOf()
            is IPv4Address -> listOf(value.toString())
            else -> throw DataConversionException("Cannot convert $value as IPv4Address")
        }
    }
}