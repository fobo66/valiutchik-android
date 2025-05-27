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
import fobo66.valiutchik.core.entities.BestRate
import fobo66.valiutchik.core.entities.Rate

@Dao
interface RatesDao {
    @Upsert
    suspend fun insertRates(rates: List<Rate>)

    @Query("SELECT max(eurBuy) AS bestRate, bankName FROM rates ORDER by date(date) DESC")
    suspend fun resolveBestEurBuyRate(): BestRate

    @Query(
        "SELECT CASE WHEN :rate = 'usdBuy' THEN max(usdBuy) " +
            "WHEN :rate = 'usdSell' THEN min(usdSell) " +
            "WHEN :rate = 'eurBuy' THEN max(eurBuy) " +
            "WHEN :rate = 'eurSell' THEN min(eurSell) " +
            "WHEN :rate = 'rubBuy' THEN max(rubBuy) " +
            "WHEN :rate = 'rubSell' THEN min(rubSell) " +
            "WHEN :rate = 'plnBuy' THEN max(plnBuy) " +
            "WHEN :rate = 'plnSell' THEN min(plnSell) " +
            "WHEN :rate = 'uahBuy' THEN max(uahBuy) " +
            "WHEN :rate = 'uahSell' THEN min(uahSell) " +
            "END AS bestRate, bankName FROM rates " +
            "WHERE bestRate != 0.0 " +
            "ORDER by date(date) DESC"
    )
    suspend fun resolveBestRate(rate: String): BestRate
}
