/*
 *    Copyright 2023 Andrey Mukamolov
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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboardImpl
import fobo66.valiutchik.domain.usecases.CurrencyRatesInteractor
import fobo66.valiutchik.domain.usecases.CurrencyRatesInteractorImpl
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
import fobo66.valiutchik.domain.usecases.RefreshExchangeRates
import fobo66.valiutchik.domain.usecases.RefreshExchangeRatesForDefaultCity
import fobo66.valiutchik.domain.usecases.RefreshExchangeRatesForDefaultCityImpl
import fobo66.valiutchik.domain.usecases.RefreshExchangeRatesImpl
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreferenceImpl
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreferenceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

  @Binds
  abstract fun provideRefreshExchangeRates(
    refreshExchangeRatesImpl: RefreshExchangeRatesImpl
  ): RefreshExchangeRates

  @Binds
  abstract fun provideForceRefreshExchangeRates(
    forceRefreshExchangeRatesImpl: ForceRefreshExchangeRatesImpl
  ): ForceRefreshExchangeRates

  @Binds
  abstract fun provideRefreshExchangeRatesForDefaultCity(
    refreshExchangeRatesForDefaultCityImpl: RefreshExchangeRatesForDefaultCityImpl
  ): RefreshExchangeRatesForDefaultCity

  @Binds
  abstract fun provideForceRefreshExchangeRatesForDefaultCity(
    forceRefreshExchangeRatesForDefaultCityImpl: ForceRefreshExchangeRatesForDefaultCityImpl
  ): ForceRefreshExchangeRatesForDefaultCity

  @Binds
  abstract fun provideLoadExchangeRates(
    loadExchangeRatesImpl: LoadExchangeRatesImpl
  ): LoadExchangeRates

  @Binds
  abstract fun provideCurrencyRatesInteractor(
    currencyRatesInteractorImpl: CurrencyRatesInteractorImpl
  ): CurrencyRatesInteractor

  @Binds
  abstract fun provideCopyCurrencyRateToClipboard(
    copyCurrencyRateToClipboardImpl: CopyCurrencyRateToClipboardImpl
  ): CopyCurrencyRateToClipboard

  @Binds
  abstract fun provideFindBankOnMap(
    findBankOnMapImpl: FindBankOnMapImpl
  ): FindBankOnMap

  @Binds
  abstract fun provideLoadDefaultCityPreference(
    loadDefaultCityPreferenceImpl: LoadDefaultCityPreferenceImpl
  ): LoadDefaultCityPreference

  @Binds
  abstract fun provideLoadUpdateIntervalPreference(
    loadUpdateIntervalPreferenceImpl: LoadUpdateIntervalPreferenceImpl
  ): LoadUpdateIntervalPreference

  @Binds
  abstract fun provideUpdateDefaultCityPreference(
    updateDefaultCityPreferenceImpl: UpdateDefaultCityPreferenceImpl
  ): UpdateDefaultCityPreference

  @Binds
  abstract fun provideUpdateUpdateIntervalPreference(
    updateUpdateIntervalPreferenceImpl: UpdateUpdateIntervalPreferenceImpl
  ): UpdateUpdateIntervalPreference

  @Binds
  abstract fun provideLoadOpenSourceLicenses(
    loadOpenSourceLicensesImpl: LoadOpenSourceLicensesImpl
  ): LoadOpenSourceLicenses
}
