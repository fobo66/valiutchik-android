package fobo66.exchangecourcesbelarus.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fobo66.exchangecourcesbelarus.entities.BestCourse
import kotlinx.coroutines.flow.Flow

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/18/19.
 */
@Dao
interface CurrencyRatesDao {

  @Query("SELECT * FROM best_rates WHERE is_buy = :isBuy ORDER BY timestamp DESC LIMIT 3")
  fun loadLatestBestCurrencyRates(isBuy: Boolean): Flow<List<BestCourse>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestCurrencyRates(bestRates: List<BestCourse>)
}
