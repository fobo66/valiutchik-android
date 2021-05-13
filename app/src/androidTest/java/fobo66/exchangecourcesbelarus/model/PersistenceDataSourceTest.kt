package fobo66.exchangecourcesbelarus.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import fobo66.exchangecourcesbelarus.db.CurrencyRatesDatabase
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUB
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.USD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

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
      context, CurrencyRatesDatabase::class.java
    ).build()
    persistenceDataSource =
      PersistenceDataSource(db)
  }

  @After
  @Throws(IOException::class)
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

  @ExperimentalTime
  @ExperimentalCoroutinesApi
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

      db.currencyRatesDao().loadLatestBestCurrencyRates(SELL_COURSE)
        .test {
          assertEquals(2, expectItem().size)
        }
    }
  }
}