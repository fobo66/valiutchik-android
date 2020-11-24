package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUR
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.util.CurrencyRatesParser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.InputStream
import java.time.LocalDateTime

/**
 * Test cases for my algorithm. Check that value resolved as best is correct based on real
 * data from server saved in resources
 */
class BestCourseProducerTest {
  private lateinit var evaluator: BestCourseProducer
  private lateinit var bestBuy: List<BestCourse>
  private lateinit var bestSell: List<BestCourse>
  private lateinit var testFile: InputStream

  private val timestamp = LocalDateTime.now().toString()

  private val parser: CurrencyRatesParser by lazy {
    TestMyfinParser()
  }

  @Before
  fun setUp() {
    evaluator = BestCourseProducer()
    testFile = javaClass.classLoader?.getResourceAsStream("data.xml")!!
    val currencies = parser.parse(testFile)

    bestBuy = evaluator.findBestBuyCourses(currencies, timestamp)
    bestSell = evaluator.findBestSellCourses(currencies, timestamp)
  }

  @Test
  fun testBestUSDBuyCoursesAreReallyBest() {
    assertEquals(USD, bestBuy[USD_INDEX].currencyName)
    assertEquals("1.925", bestBuy[USD_INDEX].currencyValue)
  }

  @Test
  fun testBestRURBuyCourseAreReallyBest() {
    assertEquals(RUR, bestBuy[RUR_INDEX].currencyName)
    assertEquals("0.0324", bestBuy[RUR_INDEX].currencyValue)
  }

  @Test
  fun testBestEURBuyCourseAreReallyBest() {
    assertEquals(EUR, bestBuy[EUR_INDEX].currencyName)
    assertEquals("2.075", bestBuy[EUR_INDEX].currencyValue)
  }

  @Test
  fun testBestUSDSellCoursesAreReallyBest() {
    assertEquals(USD, bestSell[USD_INDEX].currencyName)
    assertEquals("1.914", bestSell[USD_INDEX].currencyValue)
  }

  @Test
  fun testBestRURSellCourseAreReallyBest() {
    assertEquals(RUR, bestSell[RUR_INDEX].currencyName)
    assertEquals("0.0323", bestSell[RUR_INDEX].currencyValue)
  }

  @Test
  fun testBestEURSellCourseAreReallyBest() {
    assertEquals(EUR, bestSell[EUR_INDEX].currencyName)
    assertEquals("2.038", bestSell[EUR_INDEX].currencyValue)
  }

  companion object {
    const val USD_INDEX = 0
    const val EUR_INDEX = 1
    const val RUR_INDEX = 2
  }
}