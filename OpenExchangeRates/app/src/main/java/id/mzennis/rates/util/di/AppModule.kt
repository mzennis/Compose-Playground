package id.mzennis.rates.util.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.mzennis.rates.util.appSessionDataStore
import id.mzennis.rates.util.ktorClient
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideKtorClient(): HttpClient {
        return ktorClient
    }

    @Provides
    fun provideAppSession(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.appSessionDataStore
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}