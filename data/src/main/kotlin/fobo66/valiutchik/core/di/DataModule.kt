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

import fobo66.valiutchik.api.di.apiModule
import fobo66.valiutchik.core.model.datasource.AssetsDataSource
import fobo66.valiutchik.core.model.datasource.AssetsDataSourceImpl
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.model.datasource.BestCourseDataSourceImpl
import fobo66.valiutchik.core.model.datasource.ClipboardDataSource
import fobo66.valiutchik.core.model.datasource.ClipboardDataSourceImpl
import fobo66.valiutchik.core.model.datasource.DataStorePreferencesDataSourceImpl
import fobo66.valiutchik.core.model.datasource.GeocodingDataSource
import fobo66.valiutchik.core.model.datasource.GeocodingDataSourceImpl
import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.IntentDataSourceImpl
import fobo66.valiutchik.core.model.datasource.JsonDataSource
import fobo66.valiutchik.core.model.datasource.JsonDataSourceImpl
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSourceImpl
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSourceImpl
import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSourceImpl
import fobo66.valiutchik.core.model.repository.ClipboardRepository
import fobo66.valiutchik.core.model.repository.ClipboardRepositoryImpl
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.model.repository.CurrencyRateRepositoryImpl
import fobo66.valiutchik.core.model.repository.CurrencyRatesTimestampRepository
import fobo66.valiutchik.core.model.repository.CurrencyRatesTimestampRepositoryImpl
import fobo66.valiutchik.core.model.repository.LicensesRepository
import fobo66.valiutchik.core.model.repository.LicensesRepositoryImpl
import fobo66.valiutchik.core.model.repository.LocationRepository
import fobo66.valiutchik.core.model.repository.LocationRepositoryImpl
import fobo66.valiutchik.core.model.repository.MapRepository
import fobo66.valiutchik.core.model.repository.MapRepositoryImpl
import fobo66.valiutchik.core.model.repository.PreferenceRepository
import fobo66.valiutchik.core.model.repository.PreferenceRepositoryImpl
import fobo66.valiutchik.core.util.BankNameNormalizer
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val coroutineDispatchersModule = module {
  single(qualifier(Dispatcher.IO)) { Dispatchers.IO }
}

val dataSourcesModule = module {
  includes(apiModule, systemModule, coroutineDispatchersModule, thirdPartyModule)

  single<AssetsDataSource> {
    AssetsDataSourceImpl(get())
  }

  single<BestCourseDataSource> {
    BestCourseDataSourceImpl()
  }

  single<ClipboardDataSource> {
    ClipboardDataSourceImpl(androidContext())
  }

  single<GeocodingDataSource> {
    GeocodingDataSourceImpl(get())
  }

  single<IntentDataSource> {
    IntentDataSourceImpl(androidContext())
  }

  single<JsonDataSource> {
    JsonDataSourceImpl(get())
  }

  single<LocationDataSource> {
    LocationDataSourceImpl(androidContext(), get(qualifier(Dispatcher.IO)))
  }

  single<PersistenceDataSource> {
    PersistenceDataSourceImpl(get())
  }

  single<PreferencesDataSource> {
    DataStorePreferencesDataSourceImpl(get())
  }

  single<UriDataSource> {
    UriDataSourceImpl()
  }

  singleOf(::BankNameNormalizer)
}

val repositoriesModule = module {
  includes(dataSourcesModule)

  single<ClipboardRepository> {
    ClipboardRepositoryImpl(get())
  }

  single<CurrencyRateRepository> {
    CurrencyRateRepositoryImpl(get(), get(), get(), get(), get(qualifier(Dispatcher.IO)))
  }

  single<CurrencyRatesTimestampRepository> {
    CurrencyRatesTimestampRepositoryImpl(get())
  }

  single<LicensesRepository> {
    LicensesRepositoryImpl(get(), get())
  }

  single<LocationRepository> {
    LocationRepositoryImpl(get(), get())
  }

  single<MapRepository> {
    MapRepositoryImpl(get(), get())
  }

  single<PreferenceRepository> {
    PreferenceRepositoryImpl(get())
  }
}
