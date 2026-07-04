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

import androidx.collection.mutableScatterSetOf
import dev.fobo66.valiutchik.core.db.Bank
import fobo66.valiutchik.api.ApiDataSource
import fobo66.valiutchik.api.entity.UNDEFINED_BUY_RATE
import fobo66.valiutchik.api.entity.UNDEFINED_SELL_RATE
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.entities.toBank
import fobo66.valiutchik.core.entities.toRate
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toSet
import kotlinx.io.IOException

class CurrencyRateRepositoryImpl(
    private val persistenceDataSource: PersistenceDataSource,
    private val apiDataSource: ApiDataSource
) : CurrencyRateRepository {

    override suspend fun refreshExchangeRates(city: Long, defaultCity: Long) {
        val currencies = persistenceDataSource.loadCurrencies().first()
            .map { it.name }
        val rawRates =
            try {
                apiDataSource.loadExchangeRates(currencies, city)
            } catch (e: IOException) {
                throw CurrencyRatesLoadFailedException(e)
            }

        val banks = mutableScatterSetOf<Bank>()

        val rates = rawRates.asFlow()
            .onEach { banks.add(it.toBank()) }
            .map { it.toRate() }
            .toSet()

        with(persistenceDataSource) {
            saveBanks(banks.asSet())
            saveRates(rates)
        }
    }

    override suspend fun cleanUpOutdatedRates(): Int {
        val oldRates = persistenceDataSource.loadOldRates()

        if (oldRates.isNotEmpty()) {
            persistenceDataSource.deleteRates(oldRates)
        }

        return oldRates.size
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loadExchangeRates(): Flow<List<BestCourse>> =
        persistenceDataSource.readBestBuyCourses()
            .combine(persistenceDataSource.readBestSellCourses()) {
                    buyRates,
                    sellRates
                ->
                buyRates.map {
                    BestCourse(
                        bankName = it.bankName,
                        currencyValue = it.max ?: UNDEFINED_BUY_RATE,
                        currencyName = it.currencyId,
                        currencyId = it.sortOrder,
                        multiplier = it.multiplier,
                        isBuy = true
                    )
                } +
                    sellRates.map {
                        BestCourse(
                            bankName = it.bankName,
                            currencyValue =
                                it.min ?: UNDEFINED_SELL_RATE,
                            currencyName = it.currencyId,
                            currencyId = it.sortOrder,
                            multiplier = it.multiplier
                        )
                    }
            }
            .map { courses ->
                courses
                    .filter {
                        it.currencyValue != UNDEFINED_BUY_RATE &&
                            it.currencyValue != UNDEFINED_SELL_RATE
                    }
                    .sortedBy { it.currencyId }
            }
}
