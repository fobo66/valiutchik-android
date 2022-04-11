package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.fake.FakeBestCourseDataSource
import fobo66.exchangecourcesbelarus.model.fake.FakeCurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.fake.FakePersistenceDataSource
import fobo66.exchangecourcesbelarus.util.BankNameNormalizer
import fobo66.exchangecourcesbelarus.util.CurrencyRatesLoadFailedException
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/30/19.
 */
@ExperimentalCoroutinesApi
class CurrencyRateRepositoryTest {

  private lateinit var currencyRateRepository: CurrencyRateRepository

  private val bestCourseDataSource = FakeBestCourseDataSource()
  private val persistenceDataSource = FakePersistenceDataSource()
  private val currencyRatesDataSource = FakeCurrencyRatesDataSource()

  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  @Before
  fun setUp() {
    currencyRateRepository = CurrencyRateRepositoryImpl(
      bestCourseDataSource,
      persistenceDataSource,
      currencyRatesDataSource,
      BankNameNormalizer(),
      ioDispatcher
    )
  }

  @After
  fun tearDown() {
    persistenceDataSource.reset()
    currencyRatesDataSource.reset()
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
