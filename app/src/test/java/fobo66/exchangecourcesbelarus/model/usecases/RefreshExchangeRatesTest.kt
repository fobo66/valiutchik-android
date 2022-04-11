package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.fake.FakeCurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.fake.FakeCurrencyRatesTimestampRepository
import fobo66.exchangecourcesbelarus.model.fake.FakeLocationRepository
import fobo66.valiutchik.core.usecases.RefreshExchangeRates
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RefreshExchangeRatesTest {
  private val locationRepository = FakeLocationRepository()
  private val timestampRepository = FakeCurrencyRatesTimestampRepository()
  private val currencyRateRepository = FakeCurrencyRateRepository()

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
    timestampRepository.reset()
    currencyRateRepository.reset()
  }

  @Test
  fun `refresh exchange rates`() = runTest(UnconfinedTestDispatcher()) {
    refreshExchangeRates.execute(now)
    assertTrue(currencyRateRepository.isRefreshed)
  }

  @Test
  fun `do not refresh recent exchange rates`() = runTest(UnconfinedTestDispatcher()) {
    timestampRepository.isNeededToUpdateCurrencyRates = false

    refreshExchangeRates.execute(now)
    assertFalse(currencyRateRepository.isRefreshed)
  }

  @Test
  fun `do not resolve location for recent exchange rates`() = runTest(UnconfinedTestDispatcher()) {
    timestampRepository.isNeededToUpdateCurrencyRates = false

    refreshExchangeRates.execute(now)
    assertFalse(locationRepository.isResolved)
  }
}
