package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRatesTimestampRepository
import fobo66.valiutchik.core.model.repository.LocationRepository
import fobo66.valiutchik.core.usecases.RefreshExchangeRates
import java.time.LocalDateTime
import javax.inject.Inject

class RefreshExchangeRatesImpl @Inject constructor(
  private val locationRepository: LocationRepository,
  private val timestampRepository: CurrencyRatesTimestampRepository,
  private val currencyRateRepository: CurrencyRateRepository
) : RefreshExchangeRates {
  override suspend fun execute(now: LocalDateTime) {
    if (timestampRepository.isNeededToUpdateCurrencyRates(now)) {
      val city = locationRepository.resolveUserCity()

      currencyRateRepository.refreshExchangeRates(city, now)
      timestampRepository.saveTimestamp(now)
    }
  }
}
