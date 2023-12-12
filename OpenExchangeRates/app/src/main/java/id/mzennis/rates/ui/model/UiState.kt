package id.mzennis.rates.ui.model

import id.mzennis.rates.util.NetworkService

data class UiState(
    val currencyCodes: List<String>,
    val lastUpdated: String,
    val convertedAmount: String,
    val screenState: ScreenState,
    val appId: String = NetworkService.APP_ID
) {
    companion object {
        val Init = UiState(
            emptyList(),
            "",
            "0.0",
            ScreenState.Loading
        )
    }
}

enum class ScreenState {
    Available, Loading, UnAvailable
}