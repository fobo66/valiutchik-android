package fobo66.exchangecourcesbelarus.entities

import fobo66.exchangecourcesbelarus.util.USD
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/19/19.
 */
class BestCourseTest {

  private val bestBuyCourse = BestCourse("test", "1.925", USD, true)
  private val bestSellCourse = BestCourse("test", "1.925", USD, false)

  @Test
  fun toBuyRateCorrectConversion() {
    val buyRate = bestBuyCourse.toBestBuyRate()
    assertEquals(bestBuyCourse.currencyValue, buyRate.currencyValue)
  }

  @Test(expected = IllegalStateException::class)
  fun toBuyRateIncorrectConversion() {
    bestSellCourse.toBestBuyRate()
  }

  @Test
  fun toSellRateCorrectConversion() {
    val sellRate = bestSellCourse.toBestSellRate()
    assertEquals(bestSellCourse.currencyValue, sellRate.currencyValue)
  }

  @Test(expected = IllegalStateException::class)
  fun toSellRateIncorrectConversion() {
    bestBuyCourse.toBestSellRate()
  }

}