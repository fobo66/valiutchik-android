/*
 *    Copyright 2024 Andrey Mukamolov
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

import androidx.collection.ScatterMap
import androidx.collection.mutableScatterMapOf
import fobo66.valiutchik.api.CurrencyRatesDataSource
import fobo66.valiutchik.api.entity.Currency
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.util.BankNameNormalizer
import fobo66.valiutchik.core.util.resolveCurrencyBuyRate
import fobo66.valiutchik.core.util.resolveCurrencySellRate
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

class CurrencyRateRepositoryImpl(
  private val bestCourseDataSource: BestCourseDataSource,
  private val persistenceDataSource: PersistenceDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource,
  private val bankNameNormalizer: BankNameNormalizer
) : CurrencyRateRepository {

  private val citiesMap: ScatterMap<String, String> by lazy {
    mutableScatterMapOf(
      "Minsk" to "1",
      "Vitsebsk" to "2",
      "Baranavichy" to "20",
      "Babruysk" to "21",
      "Barysaw" to "22",
      "Lida" to "23",
      "Mazyr" to "24",
      "Navapolatsk" to "25",
      "Orsha" to "26",
      "Pinsk" to "27",
      "Polatsk" to "28",
      "Salihorsk" to "29",
      "Swislatsch" to "201",
      "Homyel" to "3",
      "Maladzyechna" to "30",
      "Svietlahorsk" to "31",
      "Zhlobin" to "32",
      "Rechytsa" to "33",
      "Sluck" to "34",
      "Zhodzina" to "35",
      "Vileyka" to "36",
      "Dzyarzhynsk" to "37",
      "Maryina Horka" to "38",
      "Horki" to "39",
      "Hrodna" to "4",
      "Asipovichy" to "40",
      "Krychaw" to "41",
      "Kalinkavichy" to "42",
      "Rahachow" to "43",
      "Brest" to "5",
      "Mahilyow" to "6"
    )
  }

  override suspend fun refreshExchangeRates(city: String, now: Instant) {
    val cityIndex = citiesMap[city] ?: "1"
    val currencies = try {
      currencyRatesDataSource.loadExchangeRates(cityIndex)
    } catch (e: IOException) {
      throw CurrencyRatesLoadFailedException(e)
    }

    val bestCourses = findBestCourses(currencies, now.toString())

    persistenceDataSource.saveBestCourses(bestCourses)
  }

  override fun loadExchangeRates(latestTimestamp: Instant): Flow<List<BestCourse>> =
    persistenceDataSource.readBestCourses(latestTimestamp)

  private fun findBestCourses(
    currencies: Set<Currency>,
    now: String
  ): List<BestCourse> = resolveBuyRates(currencies, now) + resolveSellRates(currencies, now)

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
