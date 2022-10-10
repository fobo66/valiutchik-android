package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.domain.fake.FakeCurrencyRateRepository
import fobo66.valiutchik.domain.fake.FakeCurrencyRatesTimestampRepository
import fobo66.valiutchik.domain.fake.FakeLocationRepository
import fobo66.valiutchik.domain.fake.FakePreferenceRepository
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RefreshExchangeRatesTest {
  private val locationRepository = FakeLocationRepository()
  private val timestampRepository = FakeCurrencyRatesTimestampRepository()
  private val currencyRateRepository = FakeCurrencyRateRepository()
  private val preferenceRepository = FakePreferenceRepository()

  private val now = LocalDateTime.now()

  private val refreshExchangeRates: RefreshExchangeRates =
    RefreshExchangeRatesImpl(
      locationRepository,
      timestampRepository,
      currencyRateRepository,
      preferenceRepository
    )

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
