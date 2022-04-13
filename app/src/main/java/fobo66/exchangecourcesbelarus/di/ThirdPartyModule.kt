package fobo66.exchangecourcesbelarus.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.ReverseGeocodingSearchEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.exchangecourcesbelarus.db.CurrencyRatesDatabase
import javax.inject.Singleton

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/18/19.
 */
@Module
@InstallIn(SingletonComponent::class)
object ThirdPartyModule {

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
  fun provideReverseGeocodingEngine(): ReverseGeocodingSearchEngine =
    MapboxSearchSdk.getReverseGeocodingSearchEngine()

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
