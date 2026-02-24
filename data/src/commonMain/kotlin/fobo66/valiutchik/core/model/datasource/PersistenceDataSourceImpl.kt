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
import dev.fobo66.valiutchik.core.db.Currency
import dev.fobo66.valiutchik.core.db.Database
import dev.fobo66.valiutchik.core.db.LoadBestBuyRates
import dev.fobo66.valiutchik.core.db.LoadBestSellRates
import dev.fobo66.valiutchik.core.db.Rate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersistenceDataSourceImpl(private val database: Database) : PersistenceDataSource {

    override suspend fun saveRates(rates: List<Rate>) {
        rates.forEach {
            database.rateQueries.insertRate(it)
        }
    }

    override suspend fun deleteRates(rates: List<Rate>) {
        database.rateQueries.deleteRates(rates.map { it.id })
    }

    override suspend fun loadOldRates(): List<Rate> =
        database.rateQueries.loadOldRates().executeAsList()

    override suspend fun loadCurrencies(): Flow<List<Currency>> =
        database.currencyQueries.loadCurrencies().asFlow()
            .map { it.executeAsList() }

    override fun readBestBuyCourses(currencyIds: List<Long>): Flow<List<LoadBestBuyRates>> =
        database.rateQueries.loadBestBuyRates(currencyIds).asFlow()
            .map { it.executeAsList() }

    override fun readBestSellCourses(currencyIds: List<Long>): Flow<List<LoadBestSellRates>> =
        database.rateQueries.loadBestSellRates(currencyIds).asFlow()
            .map { it.executeAsList() }
}
