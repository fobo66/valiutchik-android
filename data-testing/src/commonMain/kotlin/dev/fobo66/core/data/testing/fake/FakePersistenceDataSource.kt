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

package dev.fobo66.core.data.testing.fake

import dev.fobo66.valiutchik.core.db.Bank
import dev.fobo66.valiutchik.core.db.Currency
import dev.fobo66.valiutchik.core.db.LoadBestBuyRates
import dev.fobo66.valiutchik.core.db.LoadBestSellRates
import dev.fobo66.valiutchik.core.db.Rate
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class FakePersistenceDataSource : PersistenceDataSource {
    var isSaved = false
    var isDeleted = false

    override suspend fun loadOldRates(): List<Rate> = emptyList()
    override fun loadCurrencies(): Flow<List<Currency>> = flowOf(emptyList())

    override fun readBestBuyCourses(): Flow<List<LoadBestBuyRates>> = emptyFlow()

    override fun readBestSellCourses(): Flow<List<LoadBestSellRates>> = emptyFlow()

    override suspend fun saveRates(rates: Set<Rate>) {
        isSaved = true
    }

    override suspend fun saveBanks(banks: Set<Bank>) {
        isSaved = true
    }

    override suspend fun saveCurrencies(currencies: Set<Currency>) {
        isSaved = true
    }

    override suspend fun deleteRates(rates: List<Rate>) {
        isDeleted = true
    }
}
