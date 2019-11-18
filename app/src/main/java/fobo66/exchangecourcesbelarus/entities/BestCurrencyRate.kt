package fobo66.exchangecourcesbelarus.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestBuyRate
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestSellRate

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/17/19.
 */
sealed class BestCurrencyRate {
  @Entity(tableName = "best_buy_rates")
  data class BestBuyRate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "bank") val bank: String,
    @ColumnInfo(name = "currency_name") val currencyName: String,
    @ColumnInfo(name = "currency_value") val currencyValue: String
  ) : BestCurrencyRate()

  @Entity(tableName = "best_sell_rates")
  data class BestSellRate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "bank") val bank: String,
    @ColumnInfo(name = "currency_name") val currencyName: String,
    @ColumnInfo(name = "currency_value") val currencyValue: String
  ) : BestCurrencyRate()
}

fun BestCourse.toBestCurrencyRate(): BestCurrencyRate {
  return if (isBuy) {
    BestBuyRate(0, bank, currencyName, currencyValue)
  } else {
    BestSellRate(0, bank, currencyName, currencyValue)
  }
}