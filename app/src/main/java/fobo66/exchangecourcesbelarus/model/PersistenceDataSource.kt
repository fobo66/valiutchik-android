package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.db.CurrencyRatesDatabase
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.entities.toBestBuyRate
import fobo66.exchangecourcesbelarus.entities.toBestSellRate
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/19/19.
 */
class PersistenceDataSource @Inject constructor(
  private val database: CurrencyRatesDatabase
) {

  suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
    bestCourses.forEach {
      if (it.isBuy) {
        database.currencyRatesDao().insertBestBuyRate(it.toBestBuyRate())
      } else {
        database.currencyRatesDao().insertBestSellRate(it.toBestSellRate())
      }
    }
  }
}