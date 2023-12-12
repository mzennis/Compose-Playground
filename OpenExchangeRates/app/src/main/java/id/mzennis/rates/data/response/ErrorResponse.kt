package id.mzennis.rates.data.response

import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 04/11/23.
 */
data class ErrorResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String,
)