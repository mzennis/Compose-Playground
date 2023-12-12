package id.mzennis.rates.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.mzennis.rates.data.model.ExchangeRate
import id.mzennis.rates.util.keyLastUpdated
import id.mzennis.rates.util.keyRates
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) {

    suspend fun exchangeRate(): ExchangeRate {
        val pref = dataStore.data.first()
        val data = pref[keyRates].orEmpty()
        return ExchangeRate(
            data = gson.fromJson(data, object : TypeToken<Map<String, Double>>() {}.type),
            lastUpdated = pref[keyLastUpdated] ?: 0L
        )
    }

    suspend fun save(exchangeRates: Map<String, Double>, lastUpdated: Long) {
        dataStore.edit { pref ->
            pref[keyRates] = gson.toJson(exchangeRates).toString()
            pref[keyLastUpdated] = lastUpdated
        }
    }
}