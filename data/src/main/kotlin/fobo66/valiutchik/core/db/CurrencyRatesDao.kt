/*
 *    Copyright 2023 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fobo66.valiutchik.core.entities.BestCourse
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRatesDao {

  @Query("SELECT * FROM best_rates")
  suspend fun loadAllBestCurrencyRates(): List<BestCourse>

  @Query("SELECT * FROM best_rates WHERE timestamp == :latestTimestamp ORDER BY currency_name DESC")
  fun loadLatestBestCurrencyRates(latestTimestamp: String): Flow<List<BestCourse>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBestCurrencyRates(bestRates: List<BestCourse>)
}
