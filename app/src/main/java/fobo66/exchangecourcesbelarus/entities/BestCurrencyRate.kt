package fobo66.exchangecourcesbelarus.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
  @Entity(tableName = "best_buy_rates")
  data class BestBuyRate(
    @PrimaryKey(autoGenerate = true) val buyId: Int,
    @ColumnInfo(name = "buy_bank") override var bank: String,
    @ColumnInfo(name = "buy_currency_name") @CurrencyName override var currencyName: String,
    @ColumnInfo(name = "buy_currency_value") override var currencyValue: String
  ) : BestCurrencyRate(buyId, bank, currencyName, currencyValue)

  @Entity(tableName = "best_sell_rates")
  data class BestSellRate(
    @PrimaryKey(autoGenerate = true) val sellId: Int,
    @ColumnInfo(name = "sell_bank") override var bank: String,
    @ColumnInfo(name = "sell_currency_name") @CurrencyName override var currencyName: String,
    @ColumnInfo(name = "sell_currency_value") override var currencyValue: String
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