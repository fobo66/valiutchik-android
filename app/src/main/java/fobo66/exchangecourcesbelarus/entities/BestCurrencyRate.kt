package fobo66.exchangecourcesbelarus.entities

import fobo66.exchangecourcesbelarus.util.CurrencyName

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/17/19.
 */
data class BestCurrencyRate(
  val id: Long,
  val bank: String,
  @CurrencyName val currencyName: String,
  val currencyValue: String
)