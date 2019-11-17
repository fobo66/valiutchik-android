package fobo66.exchangecourcesbelarus.db

import androidx.room.Database
import androidx.room.RoomDatabase
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestBuyRate
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestSellRate

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/18/19.
 */
@Database(entities = [BestBuyRate::class, BestSellRate::class], version = 1)
abstract class CurrencyRatesDatabase : RoomDatabase() {
  abstract fun currencyRatesDao(): CurrencyRatesDao
}