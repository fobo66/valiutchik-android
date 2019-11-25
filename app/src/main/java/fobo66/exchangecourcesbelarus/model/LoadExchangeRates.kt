package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.repository.LocationRepository
import javax.inject.Inject

interface LoadExchangeRates {
  suspend fun execute(latitude: Double, longitude: Double): List<BestCurrencyRate>
}

class LoadExchangeRatesImpl @Inject constructor(
  private val locationRepository: LocationRepository,
  private val currencyRateRepository: CurrencyRateRepository
) : LoadExchangeRates {
  override suspend fun execute(latitude: Double, longitude: Double): List<BestCurrencyRate> {
    val city = locationRepository.resolveUserCity(latitude, longitude)
    return currencyRateRepository.loadExchangeRates(city)
  }
}