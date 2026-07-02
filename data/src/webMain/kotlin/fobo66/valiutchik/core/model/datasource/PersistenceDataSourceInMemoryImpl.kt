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

import dev.fobo66.valiutchik.core.db.Bank
import dev.fobo66.valiutchik.core.db.City
import dev.fobo66.valiutchik.core.db.Currency
import dev.fobo66.valiutchik.core.db.LoadBestBuyRates
import dev.fobo66.valiutchik.core.db.LoadBestSellRates
import dev.fobo66.valiutchik.core.db.Rate
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class PersistenceDataSourceInMemoryImpl : PersistenceDataSource {
    private val ratesFlow = MutableStateFlow(emptySet<Rate>())
    private val banksFlow = MutableStateFlow(emptySet<Bank>())
    private val citiesFlow = MutableStateFlow(emptySet<City>())
    private val currenciesFlow = MutableStateFlow(emptySet<Currency>())

    override suspend fun saveRates(rates: Set<Rate>) {
        ratesFlow.emit(rates)
    }

    override suspend fun saveBanks(banks: Set<Bank>) {
        banksFlow.emit(banks)
    }

    override suspend fun saveCities(cities: Set<City>) {
        citiesFlow.emit(cities)
    }

    override suspend fun saveCurrencies(currencies: Set<Currency>) {
        currenciesFlow.emit(currencies)
    }

    override suspend fun deleteRates(rates: List<Rate>) {
        ratesFlow.update { currentRates -> currentRates.filterNot { rates.contains(it) }.toSet() }
    }

    override suspend fun loadOldRates(): List<Rate> = ratesFlow.asStateFlow()
        .map { rates -> rates.filter { Instant.parse(it.date) < Clock.System.now() } }
        .first()

    override fun loadCurrencies(): Flow<List<Currency>> = currenciesFlow.asStateFlow()
        .map { it.toList() }

    override fun readBestBuyCourses(): Flow<List<LoadBestBuyRates>> = ratesFlow.map { rates ->
        rates.groupBy { it.currencyId.toLong() }
            .map { (currencyId, currencyRates) ->
                val currency =
                    currenciesFlow.map { currencies -> currencies.find { it.id == currencyId } }
                        .first()
                val max = currencyRates.maxByOrNull { it.buyRate }
                max?.let { maxRate ->
                    val bank =
                        banksFlow.map { banks -> banks.find { it.id == maxRate.bankId } }.first()
                    LoadBestBuyRates(
                        bankName = bank?.formattedName.orEmpty(),
                        max = maxRate.buyRate,
                        currencyId = currencyId.toString(),
                        sortOrder = currencyId,
                        multiplier = currency?.multiplier ?: 1
                    )
                }
            }
            .filterNotNull()
    }

    override fun readBestSellCourses(): Flow<List<LoadBestSellRates>> = ratesFlow.map { rates ->
        rates.groupBy { it.currencyId.toLong() }
            .map { (currencyId, currencyRates) ->
                val currency =
                    currenciesFlow.map { currencies -> currencies.find { it.id == currencyId } }
                        .first()
                val max = currencyRates.minByOrNull { it.sellRate }
                max?.let { maxRate ->
                    val bank =
                        banksFlow.map { banks -> banks.find { it.id == maxRate.bankId } }.first()
                    LoadBestSellRates(
                        bankName = bank?.formattedName.orEmpty(),
                        min = maxRate.sellRate,
                        currencyId = currencyId.toString(),
                        sortOrder = currencyId,
                        multiplier = currency?.multiplier ?: 1
                    )
                }
            }
            .filterNotNull()
    }

    override fun readCities(): Flow<List<City>> = citiesFlow.asStateFlow()
        .map { it.toList() }

    override suspend fun findCityIdByName(name: String): Long? = citiesFlow.asStateFlow()
        .map { cities -> cities.find { it.name == name } }
        .map { it?.id }
        .first()
}
