/*
 *    Copyright 2024 Andrey Mukamolov
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.tomtom.sdk.search.reversegeocoder.ReverseGeocoder
import com.tomtom.sdk.search.reversegeocoder.online.OnlineReverseGeocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

@Module
@InstallIn(SingletonComponent::class)
object ThirdPartyModule {

  @Provides
  fun provideReverseGeocodingEngine(
    @ApplicationContext context: Context,
    @GeocoderAccessToken geocoderAccessToken: String
  ): ReverseGeocoder =
    OnlineReverseGeocoder.create(context, geocoderAccessToken)

  @Provides
  fun provideJson(): Json = Json

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
    @ApplicationContext context: Context
  ): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
      context.preferencesDataStoreFile("valiutchik-prefs")
    }
}

val thirdPartyModule = module {
  includes(secretsModule)
  single<ReverseGeocoder> {
    OnlineReverseGeocoder.create(androidContext(), get(qualifier(Secret.GEOCODER_ACCESS_TOKEN)))
  }

  single<Json> {
    Json
  }

  single<CurrencyRatesDatabase> {
    Room.databaseBuilder(
      androidContext(),
      CurrencyRatesDatabase::class.java,
      "currency-rates"
    ).build()
  }

  single<DataStore<Preferences>> {
    PreferenceDataStoreFactory.create {
      androidContext().preferencesDataStoreFile("valiutchik-prefs")
    }
  }
}
