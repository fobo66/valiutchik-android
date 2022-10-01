package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.fake.FakeCurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.fake.FakeCurrencyRatesTimestampRepository
import fobo66.exchangecourcesbelarus.model.fake.FakeLocationRepository
import fobo66.exchangecourcesbelarus.model.fake.FakePreferenceRepository
import fobo66.valiutchik.core.usecases.RefreshExchangeRates
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RefreshExchangeRatesTest {
  private val locationRepository = FakeLocationRepository()
  private val timestampRepository = FakeCurrencyRatesTimestampRepository()
  private val currencyRateRepository = FakeCurrencyRateRepository()
  private val preferenceRepository = FakePreferenceRepository()

  private val now = LocalDateTime.now()

  private lateinit var refreshExchangeRates: RefreshExchangeRates

  @BeforeEach
  fun setUp() {
    refreshExchangeRates =
      RefreshExchangeRatesImpl(
        locationRepository,
        timestampRepository,
        currencyRateRepository,
        preferenceRepository
      )
  }

  @AfterEach
  fun tearDown() {
    locationRepository.reset()
    timestampRepository.reset()
    currencyRateRepository.reset()
  }

  @Test
  fun `refresh exchange rates`() = runTest(UnconfinedTestDispatcher()) {
    refreshExchangeRates.execute(now)
    assertTrue(currencyRateRepository.isRefreshed)
    assertTrue(timestampRepository.isSaveTimestampCalled)
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
