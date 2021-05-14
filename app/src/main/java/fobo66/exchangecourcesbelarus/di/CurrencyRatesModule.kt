package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fobo66.exchangecourcesbelarus.model.LoadExchangeRates
import fobo66.exchangecourcesbelarus.model.LoadExchangeRatesImpl
import fobo66.exchangecourcesbelarus.model.RefreshExchangeRates
import fobo66.exchangecourcesbelarus.model.RefreshExchangeRatesImpl
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.MyfinDataSource
import fobo66.valiutchik.core.util.CurrencyRatesParser
import fobo66.valiutchik.core.util.MyfinParser

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/8/19.
 */
@Module
@InstallIn(SingletonComponent::class)
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
  abstract fun provideCurrencyRatesParser(
      myfinParser: MyfinParser
  ): CurrencyRatesParser

  @Binds
  abstract fun provideCurrencyRatesDataSource(
      myfinDataSource: MyfinDataSource
  ): CurrencyRatesDataSource
}
