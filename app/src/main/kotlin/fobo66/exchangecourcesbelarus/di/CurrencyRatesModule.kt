package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fobo66.exchangecourcesbelarus.model.usecases.CopyCurrencyRateToClipboardImpl
import fobo66.exchangecourcesbelarus.model.usecases.FindBankOnMapImpl
import fobo66.exchangecourcesbelarus.model.usecases.LoadDefaultCityPreferenceImpl
import fobo66.exchangecourcesbelarus.model.usecases.LoadExchangeRatesImpl
import fobo66.exchangecourcesbelarus.model.usecases.LoadUpdateIntervalPreferenceImpl
import fobo66.exchangecourcesbelarus.model.usecases.RefreshExchangeRatesImpl
import fobo66.exchangecourcesbelarus.model.usecases.UpdateDefaultCityPreferenceImpl
import fobo66.exchangecourcesbelarus.model.usecases.UpdateUpdateIntervalPreferenceImpl
import fobo66.valiutchik.core.usecases.LoadExchangeRates
import fobo66.valiutchik.core.usecases.LoadOpenSourceLicenses
import fobo66.valiutchik.core.usecases.LoadOpenSourceLicensesImpl
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.FindBankOnMap
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.RefreshExchangeRates
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/8/19.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class CurrencyRatesModule {

  @Binds
  abstract fun provideRefreshExchangeRates(
    refreshExchangeRatesImpl: RefreshExchangeRatesImpl
  ): RefreshExchangeRates

  @Binds
  abstract fun provideLoadExchangeRates(
    loadExchangeRatesImpl: LoadExchangeRatesImpl
  ): LoadExchangeRates

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
