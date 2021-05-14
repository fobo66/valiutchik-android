package fobo66.exchangecourcesbelarus.util

import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUB
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.util.CurrencyRatesParser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Test cases for my algorithm. Check that value resolved as best is correct based on real
 * data from server saved in resources
 */
class BestCourseDataSourceTest {
  private lateinit var bestCourseDataSource: BestCourseDataSource
  private lateinit var bestBuy: Map<String, Currency>
  private lateinit var bestSell: Map<String, Currency>

  private val testFile = javaClass.classLoader?.getResourceAsStream("myfinTestData.xml")!!

  private val parser: CurrencyRatesParser by lazy {
    TestMyfinParser()
  }

  @Before
  fun setUp() {
    bestCourseDataSource = BestCourseDataSource()
    val currencies = parser.parse(testFile)

    bestBuy = bestCourseDataSource.findBestBuyCurrencies(currencies)
    bestSell = bestCourseDataSource.findBestSellCurrencies(currencies)
  }

  @Test
  fun testBestUSDBuyCoursesAreReallyBest() {
    assertEquals(BEST_USD_BUY, bestBuy[USD]?.usdBuy)
  }

  @Test
  fun testBestRURBuyCourseAreReallyBest() {
    assertEquals(BEST_RUR_BUY, bestBuy[RUB]?.rurBuy)
  }

  @Test
  fun testBestEURBuyCourseAreReallyBest() {
    assertEquals(BEST_EUR_BUY, bestBuy[EUR]?.eurBuy)
  }

  @Test
  fun testBestUSDSellCoursesAreReallyBest() {
    assertEquals(BEST_USD_SELL, bestSell[USD]?.usdSell)
  }

  @Test
  fun testBestRURSellCourseAreReallyBest() {
    assertEquals(BEST_RUR_SELL, bestSell[RUB]?.rurSell)
  }

  @Test
  fun testBestEURSellCourseAreReallyBest() {
    assertEquals(BEST_EUR_SELL, bestSell[EUR]?.eurSell)
  }

  companion object {
    const val BEST_USD_BUY = "2.54"
    const val BEST_USD_SELL = "2.546"
    const val BEST_EUR_BUY = "3.059"
    const val BEST_EUR_SELL = "3.068"
    const val BEST_RUR_BUY = "0.03405"
    const val BEST_RUR_SELL = "0.03414"
  }
}