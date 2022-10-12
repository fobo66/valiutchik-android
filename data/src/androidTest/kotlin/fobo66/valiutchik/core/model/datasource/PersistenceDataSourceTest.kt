/*
 *    Copyright 2022 Andrey Mukamolov
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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.util.EUR
import fobo66.valiutchik.core.util.RUB
import fobo66.valiutchik.core.util.USD
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/19/19.
 */
class PersistenceDataSourceTest {

  private lateinit var db: CurrencyRatesDatabase
  private lateinit var persistenceDataSource: PersistenceDataSource

  @get:Rule
  val liveDataRule = InstantTaskExecutorRule()

  @Before
  fun setUp() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    db = Room.inMemoryDatabaseBuilder(
      context,
      CurrencyRatesDatabase::class.java
    ).build()
    persistenceDataSource =
      PersistenceDataSourceImpl(db)
  }

  @After
  fun tearDown() {
    db.close()
  }

  @Test
  fun saveBestBuyCourses() {
    val bestCourses = listOf(
      BestCourse(0, "test", "1.925", USD, "", BUY_COURSE),
      BestCourse(0, "test", "2.25", EUR, "", BUY_COURSE)
    )

    runBlocking {
      persistenceDataSource.saveBestCourses(bestCourses)
    }

    runBlocking {
      val bestBuyRates = db.currencyRatesDao().loadAllBestCurrencyRates()
      assertEquals(2, bestBuyRates.size)
    }
  }

  @Test
  fun saveBestSellCourses() {
    val bestCourses = listOf(
      BestCourse(0, "test", "1.925", USD, "", SELL_COURSE),
      BestCourse(0, "test", "2.25", EUR, "", SELL_COURSE)
    )

    runBlocking {
      persistenceDataSource.saveBestCourses(bestCourses)
    }

    runBlocking {
      val bestSellRates = db.currencyRatesDao().loadAllBestCurrencyRates()
      assertEquals(2, bestSellRates.size)
    }
  }

  @Test
  fun saveMixedCourses() {
    val bestCourses = listOf(
      BestCourse(0, "test", "1.925", USD, "", BUY_COURSE),
      BestCourse(0, "test", "2.25", EUR, "", BUY_COURSE),
      BestCourse(0, "test", "0.0325", RUB, "", SELL_COURSE)
    )

    runBlocking {
      persistenceDataSource.saveBestCourses(bestCourses)
    }

    runBlocking {
      val bestRates = db.currencyRatesDao().loadAllBestCurrencyRates()
      assertEquals(3, bestRates.size)
    }
  }

  @Test
  fun loadOnlySellCoursesFromMixedCourses() {
    runBlocking {
      val bestCourses = listOf(
        BestCourse(0, "test", "1.925", USD, "", BUY_COURSE),
        BestCourse(0, "test", "2.25", EUR, "", BUY_COURSE),
        BestCourse(0, "test", "0.0325", RUB, "", SELL_COURSE),
        BestCourse(0, "test", "2.0325", USD, "", SELL_COURSE)
      )

      persistenceDataSource.saveBestCourses(bestCourses)

      db.currencyRatesDao().loadLatestBestCurrencyRates()
        .map { courses -> courses.filter { !it.isBuy } }
        .test {
          assertEquals(2, awaitItem().size)
        }
    }
  }
}
