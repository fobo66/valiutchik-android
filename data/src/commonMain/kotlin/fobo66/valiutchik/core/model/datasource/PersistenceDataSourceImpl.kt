/*
 *    Copyright 2026 Andrey Mukamolov
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
import dev.fobo66.valiutchik.core.db.City
import dev.fobo66.valiutchik.core.db.Currency
import dev.fobo66.valiutchik.core.db.Database
import dev.fobo66.valiutchik.core.db.LoadBestBuyRates
import dev.fobo66.valiutchik.core.db.LoadBestSellRates
import dev.fobo66.valiutchik.core.db.Rate
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersistenceDataSourceImpl(
    private val database: Database,
    private val ioDispatcher: CoroutineDispatcher
) : PersistenceDataSource {

    override suspend fun saveRates(rates: Set<Rate>) = withContext(ioDispatcher) {
        database.rateQueries.transaction {
            afterCommit { Napier.d { "Saved ${rates.size} rates" } }
            rates.forEach {
                database.rateQueries.insertRate(
                    it.date,
                    it.bankId,
                    it.currencyId,
                    it.buyRate,
                    it.sellRate,
                    it.cityId
                )
            }
        }
    }

    override suspend fun deleteRates(rates: List<Rate>) = withContext(ioDispatcher) {
        database.rateQueries.transaction {
            afterCommit { Napier.d { "Deleted ${rates.size} rates" } }
            database.rateQueries.deleteRates(rates.map { it.id })
        }
    }

    override suspend fun loadOldRates(): List<Rate> =
        database.rateQueries.loadOldRates().executeAsList()

    override fun loadCurrencies(): Flow<List<Currency>> =
        database.currencyQueries.loadCurrencies().asFlow()
            .mapToList(ioDispatcher)

    override fun readBestBuyCourses(): Flow<List<LoadBestBuyRates>> =
        database.rateQueries.loadBestBuyRates().asFlow()
            .mapToList(ioDispatcher)

    override suspend fun saveBanks(banks: Set<Bank>) = withContext(ioDispatcher) {
        database.bankQueries.transaction {
            afterCommit { Napier.d { "Saved ${banks.size} banks" } }

            banks.forEach {
                database.bankQueries.insertBank(it)
            }
        }
    }

    override suspend fun saveCities(cities: Set<City>) = withContext(ioDispatcher) {
        database.cityQueries.transaction {
            afterCommit { Napier.d { "Saved ${cities.size} cities" } }

            cities.forEach {
                database.cityQueries.insertCity(it)
            }
        }
    }

    override fun readBestSellCourses(): Flow<List<LoadBestSellRates>> =
        database.rateQueries.loadBestSellRates().asFlow()
            .mapToList(ioDispatcher)

    override suspend fun saveCurrencies(currencies: Set<Currency>) = withContext(ioDispatcher) {
        database.currencyQueries.transaction {
            afterCommit { Napier.d { "Saved ${currencies.size} currencies" } }

            currencies.forEach {
                database.currencyQueries.insertCurrency(it)
            }
        }
    }
}
