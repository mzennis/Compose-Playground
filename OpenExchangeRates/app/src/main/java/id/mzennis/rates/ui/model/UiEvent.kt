package id.mzennis.rates.ui.model

sealed interface UiEvent {

    data class OpenLink(val externalLink: String) : UiEvent
}