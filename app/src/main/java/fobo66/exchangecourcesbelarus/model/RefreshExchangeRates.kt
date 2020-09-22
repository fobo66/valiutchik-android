package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.repository.LocationRepository
import java.time.LocalDateTime
import javax.inject.Inject

interface RefreshExchangeRates {
  suspend fun execute(latitude: Double, longitude: Double)
}

class RefreshExchangeRatesImpl @Inject constructor(
  private val locationRepository: LocationRepository,
  private val currencyRateRepository: CurrencyRateRepository
) : RefreshExchangeRates {
  override suspend fun execute(latitude: Double, longitude: Double) {
    val city = locationRepository.resolveUserCity(latitude, longitude)
    currencyRateRepository.refreshExchangeRates(city, LocalDateTime.now())
  }
}