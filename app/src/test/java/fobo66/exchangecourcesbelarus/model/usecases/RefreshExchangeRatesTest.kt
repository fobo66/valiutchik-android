package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRatesTimestampRepository
import fobo66.valiutchik.core.model.repository.LocationRepository
import fobo66.valiutchik.core.usecases.RefreshExchangeRates
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class RefreshExchangeRatesTest {
  private val locationRepository: LocationRepository = mockk {
    coEvery { resolveUserCity() } returns "test"
  }
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

  @Test
  fun `refresh exchange rates`() = runBlockingTest {
    refreshExchangeRates.execute(now)
    coVerify {
      currencyRateRepository.refreshExchangeRates(any(), any())
    }
  }

  @Test
  fun `do not refresh recent exchange rates`() = runBlockingTest {
    every { timestampRepository.isNeededToUpdateCurrencyRates(any()) } returns false

    refreshExchangeRates.execute(now)
    coVerify(inverse = true) {
      currencyRateRepository.refreshExchangeRates(any(), any())
    }
  }

  @Test
  fun `do not resolve location for recent exchange rates`() = runBlockingTest {
    every { timestampRepository.isNeededToUpdateCurrencyRates(any()) } returns false

    refreshExchangeRates.execute(now)
    coVerify(inverse = true) {
      locationRepository.resolveUserCity()
    }
  }
}
