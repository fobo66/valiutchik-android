package fobo66.valiutchik.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThirdPartyModule {

  @Provides
  fun provideReverseGeocodingEngine(
    @GeocoderAccessToken geocoderAccessToken: String
  ): SearchEngine =
    SearchEngine.createSearchEngine(
      SearchEngineSettings(
        accessToken = geocoderAccessToken
      )
    )

  @Provides
  fun provideMoshi(): Moshi = Moshi.Builder().build()

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): CurrencyRatesDatabase =
    Room.databaseBuilder(
      context,
      CurrencyRatesDatabase::class.java,
      "currency-rates"
    ).build()

  @Provides
  @Singleton
  fun providePreferencesDatastore(
    @ApplicationContext context: Context,
    sharedPreferences: SharedPreferences
  ): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
      migrations = listOf(
        SharedPreferencesMigration(
          produceSharedPreferences = { sharedPreferences }
        )
      )
    ) {
      context.preferencesDataStoreFile("valiutchik-prefs")
    }
}
