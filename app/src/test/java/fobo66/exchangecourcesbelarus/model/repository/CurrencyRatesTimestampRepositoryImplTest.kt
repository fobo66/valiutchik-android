package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.fake.FakePreferenceDataSource
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CurrencyRatesTimestampRepositoryImplTest {

  private lateinit var currencyRatesTimestampRepository: CurrencyRatesTimestampRepository

  private val preferencesDataSource = FakePreferenceDataSource()

  private val now = LocalDateTime.now()

  @BeforeEach
  fun setUp() {
    currencyRatesTimestampRepository = CurrencyRatesTimestampRepositoryImpl(preferencesDataSource)
  }

  @AfterEach
  fun tearDown() {
    preferencesDataSource.reset()
  }

  @Test
  fun `no timestamp - need to update`() {
    preferencesDataSource.string = ""

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `now timestamp - need to update`() {
    preferencesDataSource.string = now.toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp is old - need to update`() {
    preferencesDataSource.string = now.minusDays(1).toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp slightly in the past - no need to update`() {
    preferencesDataSource.string = now.minusHours(1).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp on the limit - no need to update`() {
    preferencesDataSource.string = now.minusHours(3).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp above customized limit - need to update`() {
    preferencesDataSource.int = 2
    preferencesDataSource.string = now.minusHours(3).toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp in the future - no need to update`() {
    preferencesDataSource.string = now.plusHours(1).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }
}
