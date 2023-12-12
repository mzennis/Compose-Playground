package id.mzennis.rates.data.response

import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 28/10/23.
 */
data class ExchangeRatesResponse(
    @SerializedName("base") val base: String,
    @SerializedName("rates") val rates: Map<String, Double>
)