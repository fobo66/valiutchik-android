package fobo66.exchangecourcesbelarus.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fobo66.exchangecourcesbelarus.entities.BestCourse

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/18/19.
 */
@Dao
interface CurrencyRatesDao {

  @Query("SELECT * FROM best_rates")
  suspend fun loadAllBestCurrencyRates(): List<BestCourse>

  @Query("SELECT * FROM best_rates WHERE timestamp = :timestamp")
  suspend fun loadBestCurrencyRates(timestamp: String): List<BestCourse>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestCurrencyRates(vararg bestRates: BestCourse)
}