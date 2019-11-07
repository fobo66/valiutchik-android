package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import fobo66.exchangecourcesbelarus.model.CurrencyRatesParser
import fobo66.exchangecourcesbelarus.model.MyfinParser
import fobo66.exchangecourcesbelarus.util.CurrencyListSanitizer
import fobo66.exchangecourcesbelarus.util.CurrencyListSanitizerImpl

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/8/19.
 */
@Module
abstract class CurrencyRatesModule {

  @Binds
  abstract fun provideCurrencyRatesParser(myfinParser: MyfinParser): CurrencyRatesParser

  @Binds
  abstract fun provideSanitizer(currencyListSanitizerImpl: CurrencyListSanitizerImpl): CurrencyListSanitizer
}