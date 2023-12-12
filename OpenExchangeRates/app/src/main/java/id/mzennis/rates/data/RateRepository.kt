package id.mzennis.rates.data

import id.mzennis.rates.data.model.ExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RateRepository @Inject constructor(
    private val cloudDataSource: CloudDataSource,
    private val localDataSource: LocalDataSource,
) {

    val exchangeRate: StateFlow<ExchangeRate>
        get() = _exchangeRate
    private val _exchangeRate = MutableStateFlow(ExchangeRate.Empty)

    suspend fun getExchangeRate() {
        val result = withContext(Dispatchers.IO) {
            try {
                val cloud = cloudDataSource.exchangeRate()
                localDataSource.save(cloud.data, cloud.lastUpdated)
                return@withContext cloud
            } catch (e: Throwable) {
                return@withContext localDataSource.exchangeRate()
            }
        }
        _exchangeRate.emit(result)
    }
}