package id.mzennis.rates.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.appSessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_session")

val keyRates = stringPreferencesKey("rates")
val keyLastUpdated = longPreferencesKey("last_updated")