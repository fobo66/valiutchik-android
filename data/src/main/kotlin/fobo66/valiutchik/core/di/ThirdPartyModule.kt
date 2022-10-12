/*
 *    Copyright 2022 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
