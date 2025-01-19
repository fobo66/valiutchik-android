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

package fobo66.valiutchik.api.di

import fobo66.valiutchik.api.CurrencyRatesDataSource
import fobo66.valiutchik.api.CurrencyRatesDataSourceImpl
import fobo66.valiutchik.api.CurrencyRatesParser
import fobo66.valiutchik.api.CurrencyRatesParserSerializerImpl
import fobo66.valiutchik.api.GeocodingDataSource
import fobo66.valiutchik.api.GeocodingDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val apiModule =
  module {
    includes(credentialsModule, networkModule, dispatchersModule)
    singleOf<CurrencyRatesParser>(::CurrencyRatesParserSerializerImpl)
    single<CurrencyRatesDataSource> {
      CurrencyRatesDataSourceImpl(
        get(),
        get(qualifier(Api.USERNAME)),
        get(qualifier(Api.PASSWORD)),
        get(qualifier(Dispatcher.IO)),
      )
    }
    single<GeocodingDataSource> {
      GeocodingDataSourceImpl(
        get(),
        get(qualifier(Api.GEOCODING_API_KEY)),
        get(qualifier(Dispatcher.IO)),
      )
    }
  }
