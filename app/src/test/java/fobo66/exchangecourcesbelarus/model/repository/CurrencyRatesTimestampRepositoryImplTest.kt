package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.TIMESTAMP
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRatesTimestampRepositoryImplTest {

  private lateinit var currencyRatesTimestampRepository: CurrencyRatesTimestampRepository

  private val preferencesDataSource = mockk<PreferencesDataSource> {
    coEvery {
      loadLong(any(), any())
    } returns 3L
  }

  private val now = LocalDateTime.now()

  @Before
  fun setUp() {
    currencyRatesTimestampRepository = CurrencyRatesTimestampRepositoryImpl(preferencesDataSource)
  }

  @Test
  fun `no timestamp - need to update`() {
    coEvery {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns ""

    runBlockingTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `now timestamp - need to update`() {
    coEvery {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.toString()

    runBlockingTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp is old - need to update`() {
    coEvery {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusDays(1).toString()

    runBlockingTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp slightly in the past - no need to update`() {
    coEvery {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusHours(1).toString()

    runBlockingTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp on the limit - no need to update`() {
    coEvery {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusHours(3).toString()

    runBlockingTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp above customized limit - need to update`() {
    coEvery {
      preferencesDataSource.loadLong(any(), any())
    } returns 2L

    coEvery {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusHours(3).toString()

    runBlockingTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }

  @Test
  fun `timestamp in the future - no need to update`() {
    coEvery {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.plusHours(1).toString()

    runBlockingTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
    }
  }
}
