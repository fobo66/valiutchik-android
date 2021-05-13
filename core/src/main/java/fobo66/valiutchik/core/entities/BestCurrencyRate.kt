package fobo66.valiutchik.core.entities

import fobo66.valiutchik.core.CurrencyName

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/17/19.
 */
data class BestCurrencyRate(
  val id: Long,
  val bankBuy: String,
  val bankSell: String,
  @CurrencyName val currencyName: String,
  val currencyValueBuy: String,
  val currencyValueSell: String
)