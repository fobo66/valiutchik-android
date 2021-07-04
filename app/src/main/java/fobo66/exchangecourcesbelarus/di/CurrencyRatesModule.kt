package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fobo66.exchangecourcesbelarus.model.usecases.CopyCurrencyRateToClipboardImpl
import fobo66.exchangecourcesbelarus.model.usecases.FindBankOnMapImpl
import fobo66.exchangecourcesbelarus.model.usecases.LoadExchangeRatesImpl
import fobo66.exchangecourcesbelarus.model.usecases.RefreshExchangeRatesImpl
import fobo66.valiutchik.core.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.core.usecases.FindBankOnMap
import fobo66.valiutchik.core.usecases.LoadExchangeRates
import fobo66.valiutchik.core.usecases.RefreshExchangeRates

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
}
