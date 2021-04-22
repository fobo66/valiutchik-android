package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.exchangecourcesbelarus.util.BankNameNormalizer
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.util.CurrencyRatesParser
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

  private val parser = mockk<CurrencyRatesParser>()
  private val bestCourseProducer = mockk<BestCourseDataSource>()
  private val persistenceDataSource = mockk<PersistenceDataSource>()
  private val preferencesDataSource = mockk<PreferencesDataSource>()
  private val currencyRatesDataSource = mockk<CurrencyRatesDataSource>()

  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  @Before
  fun setUp() {
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

    every { bestCourseProducer.findBestBuyCurrencies(any()) } returns emptyMap()
    every { bestCourseProducer.findBestSellCurrencies(any()) } returns emptyMap()

    coEvery {
      preferencesDataSource.saveString(any(), any())
    } returns Unit

    coEvery {
      persistenceDataSource.saveBestCourses(any())
    } returns Unit

    currencyRateRepository = CurrencyRateRepository(
      parser,
      bestCourseProducer,
      persistenceDataSource,
      currencyRatesDataSource,
      BankNameNormalizer(),
      ioDispatcher
    )
  }

  @After
  fun tearDown() {
    ioDispatcher.close()
  }

  private val now = LocalDateTime.now()

  @Test
  fun `load exchange rates`() {
    runBlocking {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }

    coVerify {
      persistenceDataSource.saveBestCourses(any())
    }
  }

  @Test
  fun `do not load exchange rates when there was an error`() {

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
}