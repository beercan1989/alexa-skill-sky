package uk.co.baconi.alexa.skill.sky.information

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import uk.co.baconi.alexa.skill.sky.discovery.Device

class InformationService {

    private val client = HttpClient(OkHttp) {
        install(JsonFeature)
    }

    suspend fun getInformation(device: Device) {

    }

}