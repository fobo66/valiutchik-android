package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.fake.FakePreferenceDataSource
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CurrencyRatesTimestampRepositoryImplTest {

  private val preferencesDataSource = FakePreferenceDataSource()
  private val now = LocalDateTime.now()
  private val currencyRatesTimestampRepository: CurrencyRatesTimestampRepository =
    CurrencyRatesTimestampRepositoryImpl(preferencesDataSource)

  @Test
  fun `no timestamp - need to update`() {
    preferencesDataSource.string = ""

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `now timestamp - need to update`() {
    preferencesDataSource.string = now.toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp is old - need to update`() {
    preferencesDataSource.string = now.minusDays(1).toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp slightly in the past - no need to update`() {
    preferencesDataSource.string = now.minusHours(1).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp on the limit - no need to update`() {
    preferencesDataSource.string = now.minusHours(3).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp above customized limit - need to update`() {
    preferencesDataSource.string = now.minusHours(3).toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 2.0f))
    }
  }

  @Test
  fun `timestamp in the future - no need to update`() {
    preferencesDataSource.string = now.plusHours(1).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }
}
