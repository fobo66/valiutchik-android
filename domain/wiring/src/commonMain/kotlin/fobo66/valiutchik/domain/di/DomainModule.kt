/*
 *    Copyright 2026 Andrey Mukamolov
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

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import fobo66.valiutchik.domain.usecases.CleanUpOldRates
import fobo66.valiutchik.domain.usecases.CleanUpOldRatesImpl
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
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreferenceValues
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreferenceValuesImpl
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import fobo66.valiutchik.domain.usecases.LoadExchangeRatesImpl
import fobo66.valiutchik.domain.usecases.LoadOpenSourceLicenses
import fobo66.valiutchik.domain.usecases.LoadOpenSourceLicensesImpl
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreferenceImpl
import fobo66.valiutchik.domain.usecases.RatesInteractor
import fobo66.valiutchik.domain.usecases.RatesInteractorImpl
import fobo66.valiutchik.domain.usecases.RefreshData
import fobo66.valiutchik.domain.usecases.RefreshDataImpl
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreferenceImpl
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreferenceImpl
import org.koin.dsl.module

val domainModule = module {

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
        LoadExchangeRatesImpl(get(), get())
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

    single<CleanUpOldRates> {
        CleanUpOldRatesImpl(get())
    }

    single<RefreshData> { RefreshDataImpl(get()) }

    single<LoadDefaultCityPreferenceValues> { LoadDefaultCityPreferenceValuesImpl(get(), get()) }
}

val refreshModule = module {
    single<RatesInteractor> { RatesInteractorImpl(get(), get(), get(), get(), get()) }
}

@BindingContainer
interface DomainModule {

    @Binds
    val CopyCurrencyRateToClipboardImpl.bind: CopyCurrencyRateToClipboard

    @Binds
    val FindBankOnMapImpl.bind: FindBankOnMap

    @Binds
    val ForceRefreshExchangeRatesImpl.bind: ForceRefreshExchangeRates

    @Binds
    val ForceRefreshExchangeRatesForDefaultCityImpl.bind: ForceRefreshExchangeRatesForDefaultCity

    @Binds
    val LoadDefaultCityPreferenceImpl.bind: LoadDefaultCityPreference

    @Binds
    val LoadExchangeRatesImpl.bind: LoadExchangeRates

    @Binds
    val LoadOpenSourceLicensesImpl.bind: LoadOpenSourceLicenses

    @Binds
    val LoadUpdateIntervalPreferenceImpl.bind: LoadUpdateIntervalPreference

    @Binds
    val UpdateDefaultCityPreferenceImpl.bind: UpdateDefaultCityPreference

    @Binds
    val UpdateUpdateIntervalPreferenceImpl.bind: UpdateUpdateIntervalPreference

    @Binds
    val CleanUpOldRatesImpl.bind: CleanUpOldRates

    @Binds
    val RefreshDataImpl.bind: RefreshData

    @Binds
    val LoadDefaultCityPreferenceValuesImpl.bind: LoadDefaultCityPreferenceValues

    @Binds
    val RatesInteractorImpl.bind: RatesInteractor
}
