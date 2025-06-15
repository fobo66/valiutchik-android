/*
 *    Copyright 2025 Andrey Mukamolov
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
import androidx.room.Query
import androidx.room.Upsert
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.Rate
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {
    @Upsert
    suspend fun insertRates(rates: List<Rate>)

    @Query("SELECT * FROM rates")
    suspend fun loadAllRates(): List<Rate>

    @Query(
        """
    SELECT bankName, max(usdBuy) as currencyValue, 'DOLLAR' as currencyName, datetime(date) as timestamp, 1 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, min(usdSell) as currencyValue, 'DOLLAR' as currencyName, datetime(date) as timestamp, 0 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, max(eurBuy) as currencyValue, 'EUR' as currencyName, datetime(date) as timestamp, 1 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, min(eurSell) as currencyValue, 'EUR' as currencyName, datetime(date) as timestamp, 0 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, max(rubBuy) as currencyValue, 'RUB' as currencyName, datetime(date) as timestamp, 1 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, min(rubSell) as currencyValue, 'RUB' as currencyName, datetime(date) as timestamp, 0 as isBuy FROM rates  WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, max(plnBuy) as currencyValue, 'PLN' as currencyName, datetime(date) as timestamp, 1 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, min(plnSell) as currencyValue, 'PLN' as currencyName, datetime(date) as timestamp, 0 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, max(uahBuy) as currencyValue, 'UAH' as currencyName, datetime(date) as timestamp, 1 as isBuy FROM rates WHERE timestamp >= date('now')
    UNION ALL SELECT bankName, min(uahSell) as currencyValue, 'UAH' as currencyName, datetime(date) as timestamp, 0 as isBuy FROM rates WHERE timestamp >= date('now')
    ORDER BY currencyName"""
    )
    fun resolveBestRates(): Flow<List<BestCourse>>
}
