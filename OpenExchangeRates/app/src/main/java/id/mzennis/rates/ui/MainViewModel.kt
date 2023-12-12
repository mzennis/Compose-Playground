package id.mzennis.rates.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.mzennis.rates.data.RateRepository
import id.mzennis.rates.ui.model.MainIntent
import id.mzennis.rates.ui.model.ScreenState
import id.mzennis.rates.ui.model.UiEvent
import id.mzennis.rates.ui.model.UiState
import id.mzennis.rates.util.CurrencyFormatter
import id.mzennis.rates.util.DateConverter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RateRepository,
    private val currencyFormatter: CurrencyFormatter
) : ViewModel() {

    val uiState: StateFlow<UiState>
        get() {
            return combine(_exchangeRate, _convertedAmount, _screenState) { exchangeRate, convertedAmount, screenState ->
                UiState(
                    currencyCodes = exchangeRate.data.keys.toList(),
                    lastUpdated = DateConverter.millisToDate(exchangeRate.lastUpdated),
                    convertedAmount = currencyFormatter.formatNumber(convertedAmount),
                    screenState = screenState
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2000),
                initialValue = UiState.Init
            )
        }

    val uiEvent: SharedFlow<UiEvent>
        get() = _uiEvent

    private val _screenState = MutableStateFlow(ScreenState.Loading)
    private val _exchangeRate = repository.exchangeRate
    private val _convertedAmount = MutableStateFlow(0.0)

    private val _uiEvent = MutableSharedFlow<UiEvent>()

    init {
        doUpdate()
    }

    fun onIntent(intent: MainIntent) = when (intent) {
        is MainIntent.Convert -> doConvert(intent.amount, intent.from, intent.to)
        MainIntent.UpdateRates -> doUpdate()
        is MainIntent.OpenLink -> doOpenLink(intent.externalLink)
    }

    private fun doOpenLink(externalLink: String) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.OpenLink(externalLink))
        }
    }

    private fun doConvert(amount: String, from: String, to: String) {
        if (amount.isBlank() || from.isBlank() || to.isBlank()) return
        val amountInDouble = amount.toDoubleOrNull() ?: return

        viewModelScope.launch {
            val result = convertCurrency(amountInDouble, from, to, _exchangeRate.value.data)

            _convertedAmount.emit(result)
        }
    }

    private fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String,
        exchangeRates: Map<String, Double>
    ): Double {
        val fromRate = exchangeRates[fromCurrency] ?: throw IllegalArgumentException("Invalid currency code: $fromCurrency")
        val toRate = exchangeRates[toCurrency] ?: throw IllegalArgumentException("Invalid currency code: $toCurrency")

        if (amount < 0) {
            throw IllegalArgumentException("Amount must be non-negative")
        }

        return amount * (toRate / fromRate)
    }

    private fun doUpdate() {
        viewModelScope.launch {
            _screenState.emit(ScreenState.Loading)
            try {
                repository.getExchangeRate()
                _screenState.emit(ScreenState.Available)
            } catch (e: Throwable) {
                _screenState.emit(ScreenState.UnAvailable)
            }
        }
    }
}