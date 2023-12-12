package id.mzennis.rates.data

import id.mzennis.rates.data.model.ExchangeRate
import id.mzennis.rates.util.NetworkService
import javax.inject.Inject

class CloudDataSource @Inject constructor(
    private val networkService: NetworkService,
) {
    suspend fun exchangeRate(): ExchangeRate {
        val result = networkService.getExchangeRates()
        val lastUpdated = System.currentTimeMillis()
        return ExchangeRate(result, lastUpdated)
    }
}