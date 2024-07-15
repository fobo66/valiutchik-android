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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

  @Binds
  fun provideCurrencyRatesTimestampRepository(
    currencyRatesTimestampRepositoryImpl: CurrencyRatesTimestampRepositoryImpl
  ): CurrencyRatesTimestampRepository

  @Binds
  fun provideLocationDataSource(
    locationDataSourceImpl: LocationDataSourceImpl
  ): LocationDataSource

  @Binds
  fun provideGeocodingDataSource(
    geocodingDataSourceImpl: GeocodingDataSourceImpl
  ): GeocodingDataSource

  @Binds
  fun provideLocationRepository(
    locationRepositoryImpl: LocationRepositoryImpl
  ): LocationRepository

  @Binds
  fun provideMapRepository(
    mapRepositoryImpl: MapRepositoryImpl
  ): MapRepository

  @Binds
  fun providePreferencesDataSource(
    preferencesDataSourceImpl: DataStorePreferencesDataSourceImpl
  ): PreferencesDataSource

  @Binds
  fun providePersistenceDataSource(
    persistenceDataSourceImpl: PersistenceDataSourceImpl
  ): PersistenceDataSource

  @Binds
  fun provideBestCourseDataSource(
    bestCourseDataSourceImpl: BestCourseDataSourceImpl
  ): BestCourseDataSource

  @Binds
  fun provideIntentDataSource(
    intentDataSourceImpl: IntentDataSourceImpl
  ): IntentDataSource

  @Binds
  fun provideUriDataSource(
    uriDataSourceImpl: UriDataSourceImpl
  ): UriDataSource

  @Binds
  fun provideCurrencyRateRepository(
    currencyRateRepositoryImpl: CurrencyRateRepositoryImpl
  ): CurrencyRateRepository

  @Binds
  fun providePreferenceRepository(
    preferenceRepositoryImpl: PreferenceRepositoryImpl
  ): PreferenceRepository

  @Binds
  fun provideAssetsDataSource(
    assetsDataSourceImpl: AssetsDataSourceImpl
  ): AssetsDataSource

  @Binds
  fun provideClipboardDataSource(
    clipboardDataSourceImpl: ClipboardDataSourceImpl
  ): ClipboardDataSource

  @Binds
  fun provideClipboardRepository(
    clipboardRepositoryImpl: ClipboardRepositoryImpl
  ): ClipboardRepository

  @Binds
  fun provideLicensesRepository(
    licensesRepositoryImpl: LicensesRepositoryImpl
  ): LicensesRepository
}
