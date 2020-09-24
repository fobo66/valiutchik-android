package fobo66.exchangecourcesbelarus.model.datasource

import fobo66.exchangecourcesbelarus.db.CurrencyRatesDatabase
import fobo66.exchangecourcesbelarus.entities.BestCourse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/19/19.
 */
class PersistenceDataSource @Inject constructor(
  private val database: CurrencyRatesDatabase
) {

  suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
    database.currencyRatesDao().insertBestCurrencyRates(*bestCourses.toTypedArray())
  }

  fun readBestCourses(isBuy: Boolean): Flow<List<BestCourse>> =
    database.currencyRatesDao().loadLatestBestCurrencyRates(isBuy)
}