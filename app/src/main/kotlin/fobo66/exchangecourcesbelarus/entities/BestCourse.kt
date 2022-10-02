package fobo66.exchangecourcesbelarus.entities

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fobo66.valiutchik.core.CurrencyName
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.entities.BestCurrencyRate

/**
 * Model for best available exchange courses to put into cardview
 * Created by fobo66 on 23.08.2015.
 */
@Immutable
@Entity(tableName = "best_rates")
data class BestCourse(
  @PrimaryKey(autoGenerate = true) val id: Long,
  @ColumnInfo(name = "bank") val bank: String = "",
  @ColumnInfo(name = "currency_value") val currencyValue: String = "",
  @ColumnInfo(name = "currency_name") @CurrencyName
  val currencyName: String = USD,
  @ColumnInfo(name = "timestamp") val timestamp: String = "",
  @ColumnInfo(name = "is_buy") val isBuy: Boolean = false
) {
  fun toBestCurrencyRate(@StringRes currencyNameRes: Int): BestCurrencyRate =
    BestCurrencyRate(id, bank, currencyNameRes, currencyValue)
}
