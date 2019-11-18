package fobo66.exchangecourcesbelarus.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestBuyRate
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate.BestSellRate

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/18/19.
 */
@Dao
interface CurrencyRatesDao {

  @Query("SELECT * FROM best_buy_rates")
  suspend fun loadBestBuyRates(): List<BestBuyRate>

  @Query("SELECT * FROM best_sell_rates")
  suspend fun loadBestSellRates(): List<BestSellRate>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestBuyRates(vararg bestBuyRates: BestBuyRate)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestBuyRate(bestBuyRate: BestBuyRate)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestSellRates(vararg bestSellRates: BestSellRate)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestSellRate(bestSellRate: BestSellRate)
}