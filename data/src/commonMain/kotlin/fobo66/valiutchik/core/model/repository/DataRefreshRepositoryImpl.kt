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

package fobo66.valiutchik.core.model.repository

import androidx.collection.ScatterSet
import androidx.collection.scatterSetOf
import fobo66.valiutchik.api.ApiDataSource
import fobo66.valiutchik.core.entities.DataSyncFailedException
import fobo66.valiutchik.core.entities.toBank
import fobo66.valiutchik.core.entities.toCurrency
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

class DataRefreshRepositoryImpl(
    private val apiDataSource: ApiDataSource,
    private val persistenceDataSource: PersistenceDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : DataRefreshRepository {

    private val ignoredCurrencies: ScatterSet<String> by lazy(LazyThreadSafetyMode.NONE) {
        scatterSetOf("eurusd", "usdrub", "eurrub")
    }

    override suspend fun refresh() = withContext(defaultDispatcher) {
        try {
            val currencies = async {
                apiDataSource.loadCurrencies()
                    .filterNot { ignoredCurrencies.contains(it.alias) }
                    .map { it.toCurrency() }
                    .toSet()
            }

            val banks = async {
                apiDataSource.loadBanks()
                    .map { it.toBank() }
                    .toSet()
            }

            with(persistenceDataSource) {
                saveCurrencies(currencies.await())
                saveBanks(banks.await())
            }
        } catch (e: IOException) {
            throw DataSyncFailedException(e)
        }
    }
}
