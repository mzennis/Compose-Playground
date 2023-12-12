package id.mzennis.rates.ui.model

sealed interface MainIntent {

    data object UpdateRates : MainIntent

    data class OpenLink(val externalLink: String) : MainIntent

    data class Convert(
        val from: String,
        val to: String,
        val amount: String
    ) : MainIntent
}