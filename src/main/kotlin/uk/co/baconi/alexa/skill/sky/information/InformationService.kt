package uk.co.baconi.alexa.skill.sky.information

import arrow.fx.IO
import com.github.maltalex.ineter.base.IPv4Address
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.serialization.DefaultJsonConfiguration
import kotlinx.serialization.json.Json

class InformationService {

    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json(DefaultJsonConfiguration.copy(isLenient = true)))
        }
    }

    suspend fun getInformation(ip: IPv4Address, restPort: Int) = IO<Map<String, String>> {
        client.get {
            url.apply {
                host = ip.toString()
                port = restPort
                path("as", "system", "information")
            }
        }
    }
}