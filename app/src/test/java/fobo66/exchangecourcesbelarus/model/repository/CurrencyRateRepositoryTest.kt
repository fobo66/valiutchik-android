package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.entities.Currency
import fobo66.exchangecourcesbelarus.model.CurrencyRatesParser
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import fobo66.exchangecourcesbelarus.util.TIMESTAMP_NEW
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol.HTTP_1_1
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/30/19.
 */
@ObsoleteCoroutinesApi
class CurrencyRateRepositoryTest {

  private lateinit var currencyRateRepository: CurrencyRateRepository

  private lateinit var parser: CurrencyRatesParser
  private lateinit var currencyEvaluator: CurrencyEvaluator
  private lateinit var persistenceDataSource: PersistenceDataSource
  private lateinit var preferencesDataSource: PreferencesDataSource
  private lateinit var currencyRatesDataSource: CurrencyRatesDataSource

  private val ioDispatcher = newSingleThreadContext("IO")

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

    coEvery {
      persistenceDataSource.loadBestCourses(any())
    } returns emptyList()

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

  @Test
  fun loadExchangeRates_stale_loadFromNetwork() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP_NEW)
    } returns LocalDateTime.now().minusHours(4).toString()

    runBlocking {
      currencyRateRepository.loadExchangeRates("Минск")
    }

    coVerify {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun loadExchangeRates_error_loadFromDatabase() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP_NEW)
    } returns ""

    coEvery {
      currencyRatesDataSource.loadExchangeRates(any())
    } throws Exception("test")

    runBlocking {
      currencyRateRepository.loadExchangeRates("Минск")
    }

    coVerify(inverse = true) {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun loadExchangeRates_noSavedTimestamp_loadFromServer() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP_NEW)
    } returns ""

    runBlocking {
      currencyRateRepository.loadExchangeRates("Минск")
    }

    coVerify {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun loadExchangeRates_notStale_loadFromDatabase() {
    every {
      preferencesDataSource.loadSting(TIMESTAMP_NEW)
    } returns LocalDateTime.now().minusMinutes(42).toString()

    runBlocking {
      currencyRateRepository.loadExchangeRates("Минск")
    }

    coVerify(inverse = true) {
      currencyRatesDataSource.loadExchangeRates(any())
    }
  }
}
