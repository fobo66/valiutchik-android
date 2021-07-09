package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.util.BankNameNormalizer
import fobo66.exchangecourcesbelarus.util.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.time.LocalDateTime
import java.util.concurrent.Executors

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/30/19.
 */
class CurrencyRateRepositoryTest {

  private lateinit var currencyRateRepository: CurrencyRateRepository

  private val bestCourseDataSource = mockk<BestCourseDataSource> {
    every { findBestBuyCurrencies(any()) } returns emptyMap()
    every { findBestSellCurrencies(any()) } returns emptyMap()
  }
  private val persistenceDataSource = mockk<PersistenceDataSource> {
    coEvery {
      saveBestCourses(any())
    } returns Unit
  }

  private val currencyRatesDataSource = mockk<CurrencyRatesDataSource> {
    coEvery {
      loadExchangeRates(any())
    } returns setOf(Currency())
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

  @Test(expected = CurrencyRatesLoadFailedException::class)
  fun `do not load exchange rates when there was an error`() {

    coEvery {
      currencyRatesDataSource.loadExchangeRates(any())
    } throws HttpException(Response.error<Set<Currency>>(500, "test".toResponseBody()))

    runBlocking {
      currencyRateRepository.refreshExchangeRates("Минск", now)
    }
  }
}
