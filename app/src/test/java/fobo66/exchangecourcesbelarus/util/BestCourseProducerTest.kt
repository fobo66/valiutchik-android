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
  private lateinit var sut: BestCourseProducer
  private lateinit var bestBuy: Map<String, Currency>
  private lateinit var bestSell: Map<String, Currency>

  private val testFile = javaClass.classLoader?.getResourceAsStream("myfinTestData.xml")!!

  private val parser: CurrencyRatesParser by lazy {
    TestMyfinParser()
  }

  @Before
  fun setUp() {
    sut = BestCourseProducer()
    val currencies = parser.parse(testFile)

    bestBuy = sut.findBestBuyCurrencies(currencies)
    bestSell = sut.findBestSellCurrencies(currencies)
  }

  @Test
  fun testBestUSDBuyCoursesAreReallyBest() {
    assertEquals("1.925", bestBuy[USD]?.usdBuy)
  }

  @Test
  fun testBestRURBuyCourseAreReallyBest() {
    assertEquals("0.0324", bestBuy[RUR]?.rurBuy)
  }

  @Test
  fun testBestEURBuyCourseAreReallyBest() {
    assertEquals("2.075", bestBuy[EUR]?.eurBuy)
  }

  @Test
  fun testBestUSDSellCoursesAreReallyBest() {
    assertEquals("1.914", bestSell[USD]?.usdSell)
  }

  @Test
  fun testBestRURSellCourseAreReallyBest() {
    assertEquals("0.0323", bestSell[RUR]?.rurSell)
  }

  @Test
  fun testBestEURSellCourseAreReallyBest() {
    assertEquals("2.038", bestSell[EUR]?.eurSell)
  }

  companion object {
    const val USD_INDEX = 0
    const val EUR_INDEX = 1
    const val RUR_INDEX = 2
  }
}