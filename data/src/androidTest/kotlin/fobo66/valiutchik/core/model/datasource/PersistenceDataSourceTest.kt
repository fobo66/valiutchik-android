/*
 *    Copyright 2025 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.core.model.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.util.CurrencyName.DOLLAR
import fobo66.valiutchik.core.util.CurrencyName.EUR
import fobo66.valiutchik.core.util.CurrencyName.RUB
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

private const val TIMESTAMP = "2025-03-09T18:51:06+02:00"

@SmallTest
class PersistenceDataSourceTest {
  private val db: CurrencyRatesDatabase =
    Room
      .inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CurrencyRatesDatabase::class.java,
      ).build()
  private val persistenceDataSource: PersistenceDataSource = PersistenceDataSourceImpl(db)

  @After
  fun tearDown() {
    db.close()
  }

  @Test
  fun saveBestBuyCourses() =
    runTest {
      val bestCourses =
        listOf(
          BestCourse(0, "test", 1.925, DOLLAR, TIMESTAMP, BUY_COURSE),
          BestCourse(0, "test", 2.25, EUR, TIMESTAMP, BUY_COURSE),
        )

      persistenceDataSource.saveBestCourses(bestCourses)

      val bestBuyRates = db.currencyRatesDao().loadAllBestCurrencyRates()
      assertThat(bestBuyRates).hasSize(2)
    }

  @Test
  fun saveBestSellCourses() =
    runTest {
      val bestCourses =
        listOf(
          BestCourse(0, "test", 1.925, DOLLAR, TIMESTAMP, SELL_COURSE),
          BestCourse(0, "test", 2.25, EUR, TIMESTAMP, SELL_COURSE),
        )

      persistenceDataSource.saveBestCourses(bestCourses)
      val bestSellRates = db.currencyRatesDao().loadAllBestCurrencyRates()
      assertThat(bestSellRates).hasSize(2)
    }

  @Test
  fun saveMixedCourses() =
    runTest {
      val bestCourses =
        listOf(
          BestCourse(0, "test", 1.925, DOLLAR, TIMESTAMP, BUY_COURSE),
          BestCourse(0, "test", 2.25, EUR, TIMESTAMP, BUY_COURSE),
          BestCourse(0, "test", 0.0325, RUB, TIMESTAMP, SELL_COURSE),
        )

      persistenceDataSource.saveBestCourses(bestCourses)
      val bestRates = db.currencyRatesDao().loadAllBestCurrencyRates()
      assertThat(bestRates).hasSize(3)
    }

  @Test
  fun loadOnlySellCoursesFromMixedCourses() =
    runTest {
      val bestCourses =
        listOf(
          BestCourse(0, "test", 1.925, DOLLAR, TIMESTAMP, BUY_COURSE),
          BestCourse(0, "test", 2.25, EUR, TIMESTAMP, BUY_COURSE),
          BestCourse(0, "test", 0.0325, RUB, TIMESTAMP, SELL_COURSE),
          BestCourse(0, "test", 2.0325, DOLLAR, TIMESTAMP, SELL_COURSE),
        )

      persistenceDataSource.saveBestCourses(bestCourses)

      db
        .currencyRatesDao()
        .loadLatestBestCurrencyRates(TIMESTAMP)
        .map { courses -> courses.filter { !it.isBuy } }
      .test {
        assertThat(awaitItem()).hasSize(2)
      }
    }
}
