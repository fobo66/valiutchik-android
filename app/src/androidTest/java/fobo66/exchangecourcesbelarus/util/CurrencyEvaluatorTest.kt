package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.MyfinParser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.io.InputStream

/**
 * (c) 2017 Andrey Mukamolov aka fobo66 <fobo66@protonmail.com>
 * Test cases for my algorithm.
 */
class CurrencyEvaluatorTest {
  private lateinit var evaluator: CurrencyEvaluator
  private lateinit var bestBuy: List<BestCourse>
  private lateinit var bestSell: List<BestCourse>
  private lateinit var testFile: InputStream

  private val timestamp = LocalDateTime.now().toString()

  @Before
  @Throws(Exception::class)
  fun setUp() {
    evaluator = CurrencyEvaluator(CurrencyListSanitizerImpl())
    testFile = javaClass.classLoader?.getResourceAsStream("data.xml")!!
    val parser = MyfinParser()
    val currencyTempSet = parser.parse(testFile)
    bestBuy = evaluator.findBestBuyCourses(currencyTempSet, timestamp)
    bestSell = evaluator.findBestSellCourses(currencyTempSet, timestamp)
  }

  @Test
  @Throws(Exception::class)
  fun testBestUSDBuyCoursesAreReallyBest() {
    assertEquals(USD, bestBuy[USD_INDEX].currencyName)
    assertEquals("1.925", bestBuy[USD_INDEX].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestRURBuyCourseAreReallyBest() {
    assertEquals(RUR, bestBuy[RUR_INDEX].currencyName)
    assertEquals("0.0324", bestBuy[RUR_INDEX].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestEURBuyCourseAreReallyBest() {
    assertEquals(EUR, bestBuy[EUR_INDEX].currencyName)
    assertEquals("2.075", bestBuy[EUR_INDEX].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestUSDSellCoursesAreReallyBest() {
    assertEquals(USD, bestSell[USD_INDEX].currencyName)
    assertEquals("1.914", bestSell[USD_INDEX].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestRURSellCourseAreReallyBest() {
    assertEquals(RUR, bestSell[RUR_INDEX].currencyName)
    assertEquals("0.0323", bestSell[RUR_INDEX].currencyValue)
  }

  @Test
  @Throws(Exception::class)
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
