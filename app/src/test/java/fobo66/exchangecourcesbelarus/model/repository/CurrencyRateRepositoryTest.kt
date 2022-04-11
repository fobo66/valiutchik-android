package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.util.BankNameNormalizer
import fobo66.exchangecourcesbelarus.util.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/30/19.
 */
@ExperimentalCoroutinesApi
class CurrencyRateRepositoryTest {

  private lateinit var currencyRateRepository: CurrencyRateRepository

  private val bestCourseDataSource = object : BestCourseDataSource {
    override fun findBestBuyCurrencies(courses: Set<Currency>): Map<String, Currency> = emptyMap()

    override fun findBestSellCurrencies(courses: Set<Currency>): Map<String, Currency> = emptyMap()
  }
  private val persistenceDataSource = object : PersistenceDataSource {
    var isSaved = false

    override suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
      isSaved = true
    }

    override fun readBestCourses(): Flow<List<BestCourse>> = emptyFlow()
  }

  private val currencyRatesDataSource = object : CurrencyRatesDataSource {
    var isError = false
    override suspend fun loadExchangeRates(city: String): Set<Currency> =
      if (isError) {
        throw HttpException(Response.error<Set<Currency>>(500, "test".toResponseBody()))
      } else {
        setOf(Currency())
      }
  }

  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  @Before
  fun setUp() {
    currencyRateRepository = CurrencyRateRepository(
      bestCourseDataSource,
      persistenceDataSource,
      currencyRatesDataSource,
      BankNameNormalizer(),
      ioDispatcher
    )
  }

  @After
  fun tearDown() {
    persistenceDataSource.isSaved = false
    currencyRatesDataSource.isError = false
    ioDispatcher.close()
  }

  private val now = LocalDateTime.now()

  @Test
  fun `load exchange rates`() {
    runTest {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }

    assertTrue(persistenceDataSource.isSaved)
  }

  @Test(expected = CurrencyRatesLoadFailedException::class)
  fun `do not load exchange rates when there was an error`() {
    currencyRatesDataSource.isError = true

    runTest {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }
  }
}
