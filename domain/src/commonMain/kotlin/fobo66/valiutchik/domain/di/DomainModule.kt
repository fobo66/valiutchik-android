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

package fobo66.valiutchik.domain.di

import fobo66.valiutchik.core.di.repositoriesModule
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboardImpl
import fobo66.valiutchik.domain.usecases.FindBankOnMap
import fobo66.valiutchik.domain.usecases.FindBankOnMapImpl
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRates
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRatesForDefaultCity
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRatesForDefaultCityImpl
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRatesImpl
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreferenceImpl
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import fobo66.valiutchik.domain.usecases.LoadExchangeRatesImpl
import fobo66.valiutchik.domain.usecases.LoadOpenSourceLicenses
import fobo66.valiutchik.domain.usecases.LoadOpenSourceLicensesImpl
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreferenceImpl
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreferenceImpl
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreferenceImpl
import org.koin.dsl.module

val domainModule = module {
    includes(repositoriesModule)

    single<CopyCurrencyRateToClipboard> {
        CopyCurrencyRateToClipboardImpl(get())
    }

    single<FindBankOnMap> {
        FindBankOnMapImpl(get())
    }

    single<ForceRefreshExchangeRates> {
        ForceRefreshExchangeRatesImpl(get(), get(), get())
    }

    single<ForceRefreshExchangeRatesForDefaultCity> {
        ForceRefreshExchangeRatesForDefaultCityImpl(get(), get())
    }

    single<LoadDefaultCityPreference> {
        LoadDefaultCityPreferenceImpl(get())
    }

    single<LoadExchangeRates> {
        LoadExchangeRatesImpl(get())
    }

    single<LoadOpenSourceLicenses> {
        LoadOpenSourceLicensesImpl(get())
    }

    single<LoadUpdateIntervalPreference> {
        LoadUpdateIntervalPreferenceImpl(get())
    }

    single<UpdateDefaultCityPreference> {
        UpdateDefaultCityPreferenceImpl(get())
    }

    single<UpdateUpdateIntervalPreference> {
        UpdateUpdateIntervalPreferenceImpl(get())
    }
}
