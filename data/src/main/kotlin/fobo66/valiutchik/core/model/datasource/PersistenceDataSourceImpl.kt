package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import fobo66.valiutchik.core.entities.BestCourse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/19/19.
 */
class PersistenceDataSourceImpl @Inject constructor(
  private val database: CurrencyRatesDatabase
) : PersistenceDataSource {

  override suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
    database.currencyRatesDao().insertBestCurrencyRates(bestCourses)
  }

  override fun readBestCourses(): Flow<List<BestCourse>> =
    database.currencyRatesDao().loadLatestBestCurrencyRates()
}
