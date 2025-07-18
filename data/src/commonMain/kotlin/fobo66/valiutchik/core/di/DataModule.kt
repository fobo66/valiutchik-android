/*
 *    Copyright 2025 Andrey Mukamolov
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
import fobo66.valiutchik.core.model.datasource.DataStorePreferencesDataSourceImpl
import fobo66.valiutchik.core.model.datasource.JsonDataSource
import fobo66.valiutchik.core.model.datasource.JsonDataSourceImpl
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSourceImpl
import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.model.repository.ClipboardRepository
import fobo66.valiutchik.core.model.repository.ClipboardRepositoryImpl
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.model.repository.CurrencyRateRepositoryImpl
import fobo66.valiutchik.core.model.repository.LicensesRepository
import fobo66.valiutchik.core.model.repository.LicensesRepositoryImpl
import fobo66.valiutchik.core.model.repository.LocationRepository
import fobo66.valiutchik.core.model.repository.LocationRepositoryImpl
import fobo66.valiutchik.core.model.repository.MapRepository
import fobo66.valiutchik.core.model.repository.MapRepositoryImpl
import fobo66.valiutchik.core.model.repository.PreferenceRepository
import fobo66.valiutchik.core.model.repository.PreferenceRepositoryImpl
import fobo66.valiutchik.core.util.BankNameNormalizer
import fobo66.valiutchik.core.util.BankNameNormalizerImpl
import org.koin.dsl.module

val dataSourcesModule =
    module {
        includes(apiModule, systemModule, thirdPartyModule)

        single<JsonDataSource> {
            JsonDataSourceImpl(get())
        }

        single<PersistenceDataSource> {
            PersistenceDataSourceImpl(get())
        }

        single<PreferencesDataSource> {
            DataStorePreferencesDataSourceImpl(get())
        }

        single<BankNameNormalizer> {
            BankNameNormalizerImpl()
        }
    }

val repositoriesModule =
    module {
        includes(dataSourcesModule)

        single<ClipboardRepository> {
            ClipboardRepositoryImpl(get())
        }

        single<CurrencyRateRepository> {
            CurrencyRateRepositoryImpl(get(), get(), get())
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
