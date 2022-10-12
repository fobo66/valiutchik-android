/*
 *    Copyright 2022 Andrey Mukamolov
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

import fobo66.valiutchik.api.Currency
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.di.Io
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.model.datasource.CurrencyRatesDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.util.BankNameNormalizer
import fobo66.valiutchik.core.util.resolveCurrencyBuyRate
import fobo66.valiutchik.core.util.resolveCurrencySellRate
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRateRepositoryImpl @Inject constructor(
  private val bestCourseDataSource: BestCourseDataSource,
  private val persistenceDataSource: PersistenceDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource,
  private val bankNameNormalizer: BankNameNormalizer,
  @Io private val ioDispatcher: CoroutineDispatcher
) : CurrencyRateRepository {

  override suspend fun refreshExchangeRates(city: String, now: LocalDateTime) {
    val currencies = try {
      currencyRatesDataSource.loadExchangeRates(city)
    } catch (e: IOException) {
      throw CurrencyRatesLoadFailedException(e)
    }

    val bestCourses: List<BestCourse>
    withContext(ioDispatcher) {
      bestCourses = findBestCourses(currencies, now.toString())
    }
    persistenceDataSource.saveBestCourses(bestCourses)
  }

  override fun loadExchangeRates(): Flow<List<BestCourse>> =
    persistenceDataSource.readBestCourses()

  private fun findBestCourses(
    currencies: Set<Currency>,
    now: String
  ): List<BestCourse> {
    return resolveBuyRates(currencies, now) + resolveSellRates(currencies, now)
  }

  private fun resolveBuyRates(
    currencies: Set<Currency>,
    now: String
  ) = bestCourseDataSource.findBestBuyCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        bankNameNormalizer.normalize(currency.bankname),
        currency.resolveCurrencyBuyRate(currencyKey),
        currencyKey,
        now,
        BUY_COURSE
      )
    }

  private fun resolveSellRates(
    currencies: Set<Currency>,
    now: String
  ) = bestCourseDataSource.findBestSellCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        bankNameNormalizer.normalize(currency.bankname),
        currency.resolveCurrencySellRate(currencyKey),
        currencyKey,
        now,
        SELL_COURSE
      )
    }
}
