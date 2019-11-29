package fobo66.exchangecourcesbelarus.entities

import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestBuyRate
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestSellRate
import fobo66.exchangecourcesbelarus.util.CurrencyName

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/17/19.
 */
sealed class BestCurrencyRate(
  open var id: Int,
  open var bank: String,
  @CurrencyName open var currencyName: String,
  open var currencyValue: String
) {
  class BestBuyRate(
    val buyId: Int,
    override var bank: String,
    @CurrencyName override var currencyName: String,
    override var currencyValue: String
  ) : BestCurrencyRate(buyId, bank, currencyName, currencyValue)

  class BestSellRate(
    val sellId: Int,
    override var bank: String,
    @CurrencyName override var currencyName: String,
    override var currencyValue: String
  ) : BestCurrencyRate(sellId, bank, currencyName, currencyValue)
}

fun BestCourse.toBestBuyRate(): BestBuyRate {
  check(isBuy) { "Wrong value. Expected it to be buy course, but was sell course" }
  return BestBuyRate(0, bank, currencyName, currencyValue)
}

fun BestCourse.toBestSellRate(): BestSellRate {
  check(!isBuy) { "Wrong value. Expected it to be sell course, but was buy course" }
  return BestSellRate(0, bank, currencyName, currencyValue)
}