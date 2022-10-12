package fobo66.valiutchik.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fobo66.valiutchik.core.entities.BestCourse
import kotlinx.coroutines.flow.Flow

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/18/19.
 */
@Dao
interface CurrencyRatesDao {

  @Query("SELECT * FROM best_rates")
  suspend fun loadAllBestCurrencyRates(): List<BestCourse>

  @Query("SELECT * FROM best_rates ORDER BY timestamp DESC, currency_name DESC LIMIT 6")
  fun loadLatestBestCurrencyRates(): Flow<List<BestCourse>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestCurrencyRates(bestRates: List<BestCourse>)
}
