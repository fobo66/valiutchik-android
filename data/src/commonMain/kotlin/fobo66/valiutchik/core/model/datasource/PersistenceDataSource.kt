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

package fobo66.valiutchik.core.model.datasource

import dev.fobo66.valiutchik.core.db.Bank
import dev.fobo66.valiutchik.core.db.Currency
import dev.fobo66.valiutchik.core.db.LoadBestBuyRates
import dev.fobo66.valiutchik.core.db.LoadBestSellRates
import dev.fobo66.valiutchik.core.db.Rate
import kotlinx.coroutines.flow.Flow

/**
 * Datasource for working with persistent database
 */
interface PersistenceDataSource {
    /**
     * Save entries to the database
     */
    suspend fun saveRates(rates: Set<Rate>)

    /**
     * Save bank entries to the database
     */
    suspend fun saveBanks(banks: Set<Bank>)

    /**
     * Save currency entries to the database
     */
    suspend fun saveCurrencies(currencies: Set<Currency>)

    /**
     * Delete entries from the database
     */
    suspend fun deleteRates(rates: List<Rate>)

    /**
     * Load outdated entries from the database
     */
    suspend fun loadOldRates(): List<Rate>

    /**
     * Load currency entries from the database
     */
    fun loadCurrencies(): Flow<List<Currency>>

    /**
     * Read buy entries from the database
     */
    fun readBestBuyCourses(): Flow<List<LoadBestBuyRates>>

    /**
     * Read entries from the database
     */
    fun readBestSellCourses(): Flow<List<LoadBestSellRates>>
}
