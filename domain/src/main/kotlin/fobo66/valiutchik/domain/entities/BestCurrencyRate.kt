package fobo66.valiutchik.domain.entities

import androidx.annotation.StringRes

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/17/19.
 */
data class BestCurrencyRate(
  val id: Long,
  val bank: String,
  @StringRes val currencyNameRes: Int,
  val currencyValue: String
)
