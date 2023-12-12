package id.mzennis.rates.util

import id.mzennis.rates.data.response.ErrorResponse
import id.mzennis.rates.data.response.ExchangeRatesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.serialization.gson.gson
import javax.inject.Inject

val ktorClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson()
    }
}

class NetworkService @Inject constructor(
    private val ktorClient: HttpClient
)  {

    companion object {
        const val APP_ID = "eced127164574211bece271f08d89c0e"
        private const val URL = "https://openexchangerates.org/api/"
    }

    suspend fun getExchangeRates(): Map<String, Double> {
        val httpResponse = ktorClient.get(URL) {
            url {
                appendPathSegments("latest.json")
                parameters.append("app_id", APP_ID)
            }
        }
        return if (httpResponse.status.value in 200..299) {
            httpResponse.body<ExchangeRatesResponse>().rates
        } else {
            val error = httpResponse.body<ErrorResponse>()
            throw IllegalStateException(error.message)
        }
    }
}