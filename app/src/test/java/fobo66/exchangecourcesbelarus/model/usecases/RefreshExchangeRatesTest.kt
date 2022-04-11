package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.fake.FakeLocationRepository
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRatesTimestampRepository
import fobo66.valiutchik.core.usecases.RefreshExchangeRates
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RefreshExchangeRatesTest {
  private val locationRepository = FakeLocationRepository()
  private val timestampRepository: CurrencyRatesTimestampRepository = mockk {
    every { saveTimestamp(any()) } returns Unit
    every { isNeededToUpdateCurrencyRates(any()) } returns true
  }
  private val currencyRateRepository: CurrencyRateRepository = mockk {
    coEvery { refreshExchangeRates(any(), any()) } returns Unit
  }

  private val now = LocalDateTime.now()

  private lateinit var refreshExchangeRates: RefreshExchangeRates

  @Before
  fun setUp() {
    refreshExchangeRates =
      RefreshExchangeRatesImpl(locationRepository, timestampRepository, currencyRateRepository)
  }

  @After
  fun tearDown() {
    locationRepository.reset()
  }

  @Test
  fun `refresh exchange rates`() = runTest(UnconfinedTestDispatcher()) {
    refreshExchangeRates.execute(now)
    coVerify {
      currencyRateRepository.refreshExchangeRates(any(), any())
    }
  }

  @Test
  fun `do not refresh recent exchange rates`() = runTest(UnconfinedTestDispatcher()) {
    every { timestampRepository.isNeededToUpdateCurrencyRates(any()) } returns false

    refreshExchangeRates.execute(now)
    coVerify(inverse = true) {
      currencyRateRepository.refreshExchangeRates(any(), any())
    }
  }

  @Test
  fun `do not resolve location for recent exchange rates`() = runTest(UnconfinedTestDispatcher()) {
    every { timestampRepository.isNeededToUpdateCurrencyRates(any()) } returns false

    refreshExchangeRates.execute(now)
    assertFalse(locationRepository.isResolved)
  }
}
