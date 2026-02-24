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

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.fobo66.valiutchik.core.db.Bank
import dev.fobo66.valiutchik.core.db.Currency
import dev.fobo66.valiutchik.core.db.Database
import dev.fobo66.valiutchik.core.db.LoadBestBuyRates
import dev.fobo66.valiutchik.core.db.LoadBestSellRates
import dev.fobo66.valiutchik.core.db.Rate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext

class PersistenceDataSourceImpl(
    private val database: Database,
    private val ioDispatcher: CoroutineDispatcher
) : PersistenceDataSource {

    override suspend fun saveRates(rates: Set<Rate>) = withContext(ioDispatcher) {
        rates.forEach {
            database.rateQueries.insertRate(it).await()
        }
    }

    override suspend fun deleteRates(rates: List<Rate>) = withContext(ioDispatcher) {
        database.rateQueries.deleteRates(rates.map { it.id }).await()
        Unit
    }

    override suspend fun loadOldRates(): List<Rate> =
        database.rateQueries.loadOldRates().executeAsList()

    override fun loadCurrencies(): Flow<List<Currency>> =
        database.currencyQueries.loadCurrencies().asFlow()
            .mapToList(ioDispatcher)

    override fun readBestBuyCourses(currencyIds: List<String>): Flow<List<LoadBestBuyRates>> =
        database.rateQueries.loadBestBuyRates(currencyIds).asFlow()
            .mapToList(ioDispatcher)
            .catch {
                emit(emptyList())
            }

    override suspend fun saveBanks(banks: Set<Bank>) = withContext(ioDispatcher) {
        banks.forEach {
            database.bankQueries.insertBank(it).await()
        }
    }

    override fun readBestSellCourses(currencyIds: List<String>): Flow<List<LoadBestSellRates>> =
        database.rateQueries.loadBestSellRates(currencyIds).asFlow()
            .mapToList(ioDispatcher)
            .catch {
                emit(emptyList())
            }
}
