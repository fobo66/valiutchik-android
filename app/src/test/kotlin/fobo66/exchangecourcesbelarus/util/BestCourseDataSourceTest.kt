package fobo66.exchangecourcesbelarus.util

import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUB
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.model.datasource.BestCourseDataSourceImpl
import fobo66.valiutchik.core.util.CurrencyRatesParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Test cases for my algorithm. Check that value resolved as best is correct based on real
 * data from server saved in resources
 */
class BestCourseDataSourceTest {
  private val testFile = javaClass.classLoader?.getResourceAsStream("myfinTestData.xml")!!
  private val parser: CurrencyRatesParser = TestMyfinParser()
  private val currencies = parser.parse(testFile)

  private val bestCourseDataSource: BestCourseDataSource = BestCourseDataSourceImpl()
  private val bestBuy = bestCourseDataSource.findBestBuyCurrencies(currencies)
  private val bestSell = bestCourseDataSource.findBestSellCurrencies(currencies)

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
