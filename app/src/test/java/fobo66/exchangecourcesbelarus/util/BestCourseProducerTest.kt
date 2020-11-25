package fobo66.exchangecourcesbelarus.util

import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUR
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.util.CurrencyRatesParser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Test cases for my algorithm. Check that value resolved as best is correct based on real
 * data from server saved in resources
 */
class BestCourseProducerTest {
  private lateinit var bestCourseProducer: BestCourseProducer
  private lateinit var bestBuy: Map<String, Currency>
  private lateinit var bestSell: Map<String, Currency>

  private val testFile = javaClass.classLoader?.getResourceAsStream("myfinTestData.xml")!!

  private val parser: CurrencyRatesParser by lazy {
    TestMyfinParser()
  }

  @Before
  fun setUp() {
    bestCourseProducer = BestCourseProducer()
    val currencies = parser.parse(testFile)

    bestBuy = bestCourseProducer.findBestBuyCurrencies(currencies)
    bestSell = bestCourseProducer.findBestSellCurrencies(currencies)
  }

  @Test
  fun testBestUSDBuyCoursesAreReallyBest() {
    assertEquals(BEST_USD_BUY, bestBuy[USD]?.usdBuy)
  }

  @Test
  fun testBestRURBuyCourseAreReallyBest() {
    assertEquals(BEST_RUR_BUY, bestBuy[RUR]?.rurBuy)
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
    assertEquals(BEST_RUR_SELL, bestSell[RUR]?.rurSell)
  }

  @Test
  fun testBestEURSellCourseAreReallyBest() {
    assertEquals(BEST_EUR_SELL, bestSell[EUR]?.eurSell)
  }

  companion object {
    const val BEST_USD_BUY = "1.925"
    const val BEST_USD_SELL = "1.914"
    const val BEST_EUR_BUY = "2.075"
    const val BEST_EUR_SELL = "2.038"
    const val BEST_RUR_BUY = "0.0324"
    const val BEST_RUR_SELL = "0.0323"
  }
}