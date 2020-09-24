package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.entities.Currency
import fobo66.exchangecourcesbelarus.model.CurrencyRatesParser
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import fobo66.exchangecourcesbelarus.util.TIMESTAMP
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol.HTTP_1_1
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.util.concurrent.Executors

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/30/19.
 */
class CurrencyRateRepositoryTest {

  private lateinit var currencyRateRepository: CurrencyRateRepository

  private lateinit var parser: CurrencyRatesParser
  private lateinit var currencyEvaluator: CurrencyEvaluator
  private lateinit var persistenceDataSource: PersistenceDataSource
  private lateinit var preferencesDataSource: PreferencesDataSource
  private lateinit var currencyRatesDataSource: CurrencyRatesDataSource

  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  @Before
  fun setUp() {
    parser = mockk()
    currencyEvaluator = mockk()
    persistenceDataSource = mockk()
    preferencesDataSource = mockk()
    currencyRatesDataSource = mockk()

    coEvery {
      currencyRatesDataSource.loadExchangeRates(any())
    } returns Response.Builder()
      .request(
        Request.Builder()
          .url("http://www.test.com")
          .build()
      )
      .code(200)
      .protocol(HTTP_1_1)
      .message("test")
      .body("<bank></bank>".toResponseBody("text/xml".toMediaType()))
      .build()

    every { parser.parse(any()) } returns setOf(Currency())

    every { currencyEvaluator.findBestBuyCourses(any(), any()) } returns listOf()
    every { currencyEvaluator.findBestSellCourses(any(), any()) } returns listOf()

    coEvery {
      preferencesDataSource.saveString(any(), any())
    } returns Unit

    coEvery {
      persistenceDataSource.saveBestCourses(any())
    } returns Unit

    currencyRateRepository = CurrencyRateRepository(
      parser,
      currencyEvaluator,
      persistenceDataSource,
      preferencesDataSource,
      currencyRatesDataSource,
      ioDispatcher
    )
  }

  @After
  fun tearDown() {
    ioDispatcher.close()
  }

  private val now = LocalDateTime.now()

  @Test
  fun `load exchange rates from network when they are stale`() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP)
    } returns now.minusHours(4).toString()

    runBlocking {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }

    coVerify {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun `load exchange rates from database when they were just created`() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP)
    } returns now.toString()

    runBlocking {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }

    coVerify {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun `load exchange rates from database when there was an error`() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP)
    } returns ""

    coEvery {
      currencyRatesDataSource.loadExchangeRates(any())
    } throws Exception("test")

    runBlocking {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }

    coVerify(inverse = true) {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun `load exchange rates from network when they were not yet loaded`() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP)
    } returns ""

    runBlocking {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }

    coVerify {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun `load exchange rates from database when they are not stale`() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP)
    } returns now.minusMinutes(42).toString()

    runBlocking {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }

    coVerify(inverse = true) {
      currencyRatesDataSource.loadExchangeRates(any())
    }
  }
}