package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.MyfinParser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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

  @Before
  @Throws(Exception::class)
  fun setUp() {
    evaluator = CurrencyEvaluator(CurrencyListSanitizerImpl())
    testFile = javaClass.classLoader?.getResourceAsStream("data.xml")!!
    val parser = MyfinParser()
    val currencyTempSet = parser.parse(testFile)
    bestBuy = evaluator.findBestBuyCourses(currencyTempSet)
    bestSell = evaluator.findBestSellCourses(currencyTempSet)
  }

  @Test
  @Throws(Exception::class)
  fun testBestUSDBuyCoursesAreReallyBest() {
    assertEquals("USD", bestBuy[0].currencyName)
    assertEquals("1.925", bestBuy[0].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestRURBuyCourseAreReallyBest() {
    assertEquals("RUR", bestBuy[2].currencyName)
    assertEquals("0.0324", bestBuy[2].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestEURBuyCourseAreReallyBest() {
    assertEquals("EUR", bestBuy[1].currencyName)
    assertEquals("2.075", bestBuy[1].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestUSDSellCoursesAreReallyBest() {
    assertEquals("USD", bestSell[0].currencyName)
    assertEquals("1.914", bestSell[0].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestRURSellCourseAreReallyBest() {
    assertEquals("RUR", bestSell[2].currencyName)
    assertEquals("0.0323", bestSell[2].currencyValue)
  }

  @Test
  @Throws(Exception::class)
  fun testBestEURSellCourseAreReallyBest() {
    assertEquals("EUR", bestSell[1].currencyName)
    assertEquals("2.038", bestSell[1].currencyValue)
  }
}