package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.model.repository.CurrencyRatesTimestampRepository
import fobo66.valiutchik.core.model.repository.LocationRepository
import fobo66.valiutchik.core.model.repository.PreferenceRepository
import fobo66.valiutchik.domain.usecases.RefreshExchangeRates
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class RefreshExchangeRatesImpl @Inject constructor(
  private val locationRepository: LocationRepository,
  private val timestampRepository: CurrencyRatesTimestampRepository,
  private val currencyRateRepository: CurrencyRateRepository,
  private val preferenceRepository: PreferenceRepository
) : RefreshExchangeRates {
  override suspend fun execute(now: LocalDateTime) {
    val defaultCity = preferenceRepository.observeDefaultCityPreference().first()
    val updateInterval = preferenceRepository.observeUpdateIntervalPreference().first()
    if (timestampRepository.isNeededToUpdateCurrencyRates(now, updateInterval)) {
      val city = locationRepository.resolveUserCity(defaultCity)

      currencyRateRepository.refreshExchangeRates(city, now)
      timestampRepository.saveTimestamp(now)
    }
  }
}
