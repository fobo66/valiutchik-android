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

package fobo66.valiutchik.core.model.repository

import androidx.collection.ScatterMap
import androidx.collection.mutableScatterMapOf
import fobo66.valiutchik.api.CurrencyRatesDataSource
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.util.BankNameNormalizer
import fobo66.valiutchik.core.util.resolveCurrencyBuyRate
import fobo66.valiutchik.core.util.resolveCurrencySellRate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import java.io.IOException

class CurrencyRateRepositoryImpl(
  private val bestCourseDataSource: BestCourseDataSource,
  private val persistenceDataSource: PersistenceDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource,
  private val bankNameNormalizer: BankNameNormalizer,
) : CurrencyRateRepository {
  private val citiesMap: ScatterMap<String, String> by lazy {
    mutableScatterMapOf(
      "Ashmyany" to "81",
      "Asipovichy" to "40",
      "Astravyets" to "82",
      "Babruysk" to "21",
      "Baranavichy" to "20",
      "Barysaw" to "22",
      "Biaroza" to "45",
      "Braslau" to "63",
      "Brest" to "5",
      "Bykhaw" to "64",
      "Chavusy" to "68",
      "Dobrush" to "69",
      "Drahichyn" to "75",
      "Dzyarzhynsk" to "37",
      "Hantsavichy" to "76",
      "Hlybokaye" to "59",
      "Homyel" to "3",
      "Horki" to "39",
      "Hrodna" to "4",
      "Ivanava" to "74",
      "Ivatsevichy" to "47",
      "Kalinkavichy" to "42",
      "Kastsiukovichy" to "66",
      "Khoiniki" to "71",
      "Kobryn" to "44",
      "Krychaw" to "41",
      "Lepel" to "60",
      "Lida" to "23",
      "Luniniec" to "46",
      "Mahilyow" to "6",
      "Maladzyechna" to "30",
      "Malaryta" to "78",
      "Maryina Horka" to "38",
      "Masty" to "79",
      "Mazyr" to "24",
      "Minsk" to "1",
      "Mstsislau" to "67",
      "Navahrudak" to "51",
      "Navalukoml" to "61",
      "Navapolatsk" to "25",
      "Nyasvizh" to "57",
      "Orsha" to "26",
      "Pastavy" to "58",
      "Pinsk" to "27",
      "Polatsk" to "28",
      "Pruzhany" to "73",
      "Pyetrykaw" to "72",
      "Rahachow" to "43",
      "Rechytsa" to "33",
      "Salihorsk" to "29",
      "Shchuchyn" to "80",
      "Shklov" to "65",
      "Slonim" to "48",
      "Sluck" to "34",
      "Smaliavichy" to "55",
      "Smarhon" to "50",
      "Stolin" to "77",
      "Stowbtsy" to "54",
      "Svietlahorsk" to "31",
      "Swislatsch" to "201",
      "Talochyn" to "62",
      "Vawkavysk" to "49",
      "Vileyka" to "36",
      "Vitsebsk" to "2",
      "Zaslawye" to "56",
      "Zhlobin" to "32",
      "Zhodzina" to "35",
      "Zhytkavichy" to "70",
    )
  }

  override suspend fun refreshExchangeRates(
    city: String,
    now: Instant,
    defaultCity: String,
  ) {
    val cityIndex = citiesMap[city] ?: citiesMap[defaultCity] ?: "1"
    val currencies =
      try {
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
    currencies: List<Bank>,
    now: String,
  ): List<BestCourse> = resolveBuyRates(currencies, now) + resolveSellRates(currencies, now)

  private fun resolveBuyRates(
    currencies: List<Bank>,
    now: String,
  ) = bestCourseDataSource
    .findBestBuyCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        bankNameNormalizer.normalize(currency.bankName),
        currency.resolveCurrencyBuyRate(currencyKey),
        currencyKey,
        now,
        BUY_COURSE,
      )
    }

  private fun resolveSellRates(
    currencies: List<Bank>,
    now: String,
  ) = bestCourseDataSource
    .findBestSellCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        bankNameNormalizer.normalize(currency.bankName),
        currency.resolveCurrencySellRate(currencyKey),
        currencyKey,
        now,
        SELL_COURSE,
      )
    }
}
