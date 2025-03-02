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

package fobo66.valiutchik.core.util

import fobo66.valiutchik.api.CurrencyRatesParser
import fobo66.valiutchik.api.CurrencyRatesParserSerializerImpl
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.model.datasource.BestCourseDataSourceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

const val BEST_USD_BUY = "2.54"
const val BEST_USD_SELL = "2.546"
const val BEST_EUR_BUY = "3.059"
const val BEST_EUR_SELL = "3.068"
const val BEST_RUR_BUY = "0.03405"
const val BEST_RUR_SELL = "0.03414"
const val BEST_PLN_BUY = "0.654"
const val BEST_PLN_SELL = "0.6715"
const val BEST_UAH_BUY = "0.08"
const val BEST_UAH_SELL = "0.09"

/**
 * Test cases for my algorithm. Check that value resolved as best is correct based on real
 * data from server saved in resources
 */
class BestCourseDataSourceTest {
  private val testFile = javaClass.classLoader?.getResourceAsStream("myfinTestData.xml")!!
  private val parser: CurrencyRatesParser = CurrencyRatesParserSerializerImpl()
  private val currencies = parser.parse(testFile)

  private val bestCourseDataSource: BestCourseDataSource = BestCourseDataSourceImpl()
  private val bestBuy = bestCourseDataSource.findBestBuyCurrencies(currencies)
  private val bestSell = bestCourseDataSource.findBestSellCurrencies(currencies)

  @Test
  fun testBestUSDBuyCoursesAreReallyBest() {
    assertEquals(BEST_USD_BUY, bestBuy[CurrencyName.USD]?.usdBuy)
  }

  @Test
  fun testBestRURBuyCourseAreReallyBest() {
    assertEquals(BEST_RUR_BUY, bestBuy[CurrencyName.RUB]?.rubBuy)
  }

  @Test
  fun testBestEURBuyCourseAreReallyBest() {
    assertEquals(BEST_EUR_BUY, bestBuy[CurrencyName.EUR]?.eurBuy)
  }

  @Test
  fun testBestPLNBuyCourseAreReallyBest() {
    assertEquals(BEST_PLN_BUY, bestBuy[CurrencyName.PLN]?.plnBuy)
  }

  @Test
  fun testBestUAHBuyCourseAreReallyBest() {
    assertEquals(BEST_UAH_BUY, bestBuy[CurrencyName.UAH]?.uahBuy)
  }

  @Test
  fun testBestUSDSellCoursesAreReallyBest() {
    assertEquals(BEST_USD_SELL, bestSell[CurrencyName.USD]?.usdSell)
  }

  @Test
  fun testBestRURSellCourseAreReallyBest() {
    assertEquals(BEST_RUR_SELL, bestSell[CurrencyName.RUB]?.rubSell)
  }

  @Test
  fun testBestEURSellCourseAreReallyBest() {
    assertEquals(BEST_EUR_SELL, bestSell[CurrencyName.EUR]?.eurSell)
  }

  @Test
  fun testBestPLNSellCourseAreReallyBest() {
    assertEquals(BEST_PLN_SELL, bestSell[CurrencyName.PLN]?.plnSell)
  }

  @Test
  fun testBestUAHSellCourseAreReallyBest() {
    assertEquals(BEST_UAH_SELL, bestSell[CurrencyName.UAH]?.uahSell)
  }
}
