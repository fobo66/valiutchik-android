package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.fake.FakeBestCourseDataSource
import fobo66.valiutchik.core.fake.FakeCurrencyRatesDataSource
import fobo66.valiutchik.core.fake.FakePersistenceDataSource
import fobo66.valiutchik.core.util.BankNameNormalizer
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/30/19.
 */
@ExperimentalCoroutinesApi
class CurrencyRateRepositoryTest {

  private val bestCourseDataSource = FakeBestCourseDataSource()
  private val persistenceDataSource = FakePersistenceDataSource()
  private val currencyRatesDataSource = FakeCurrencyRatesDataSource()
  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  private val currencyRateRepository: CurrencyRateRepository = CurrencyRateRepositoryImpl(
    bestCourseDataSource,
    persistenceDataSource,
    currencyRatesDataSource,
    BankNameNormalizer(),
    ioDispatcher
  )

  @AfterEach
  fun tearDown() {
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

  @Test
  fun `do not load exchange rates when there was an error`() {
    currencyRatesDataSource.isError = true

    runTest {
      assertThrows<CurrencyRatesLoadFailedException> {
        currencyRateRepository.refreshExchangeRates("Минск", now)
      }
    }
  }
}
