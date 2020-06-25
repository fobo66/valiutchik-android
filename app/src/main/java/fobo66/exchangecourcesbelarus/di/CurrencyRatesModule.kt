package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import fobo66.exchangecourcesbelarus.model.CurrencyRatesParser
import fobo66.exchangecourcesbelarus.model.LoadExchangeRates
import fobo66.exchangecourcesbelarus.model.LoadExchangeRatesImpl
import fobo66.exchangecourcesbelarus.model.MyfinParser
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.MyfinDataSource
import fobo66.exchangecourcesbelarus.util.CurrencyListSanitizer
import fobo66.exchangecourcesbelarus.util.CurrencyListSanitizerImpl

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/8/19.
 */
@Module
abstract class CurrencyRatesModule {

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

  @Binds
  abstract fun provideSanitizer(
    currencyListSanitizerImpl: CurrencyListSanitizerImpl
  ): CurrencyListSanitizer
}