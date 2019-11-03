package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.Currency

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 15.03.2017.
 */
interface CurrencyBuilder {
  @Throws(NoSuchFieldException::class, IllegalAccessException::class)
  fun with(fieldName: String, fieldValue: String): CurrencyBuilder

  fun build(): Currency
}