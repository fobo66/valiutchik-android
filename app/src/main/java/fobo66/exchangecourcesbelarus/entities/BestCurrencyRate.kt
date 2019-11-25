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
sealed class BestCurrencyRate {
  @Entity(tableName = "best_buy_rates")
  data class BestBuyRate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "bank") val bank: String,
    @ColumnInfo(name = "currency_name") @CurrencyName val currencyName: String,
    @ColumnInfo(name = "currency_value") val currencyValue: String
  ) : BestCurrencyRate()

  @Entity(tableName = "best_sell_rates")
  data class BestSellRate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "bank") val bank: String,
    @ColumnInfo(name = "currency_name") @CurrencyName val currencyName: String,
    @ColumnInfo(name = "currency_value") val currencyValue: String
  ) : BestCurrencyRate()
}

fun BestCourse.toBestBuyRate(): BestBuyRate {
  check(isBuy) { "Wrong value. Expected it to be buy course, but was sell course" }
  return BestBuyRate(0, bank, currencyName, currencyValue)
}

fun BestCourse.toBestSellRate(): BestSellRate {
  check(!isBuy) { "Wrong value. Expected it to be sell course, but was buy course" }
  return BestSellRate(0, bank, currencyName, currencyValue)
}