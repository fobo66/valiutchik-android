package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.exchangecourcesbelarus.model.repository.LocationRepository
import java.time.LocalDateTime
import javax.inject.Inject

interface LoadExchangeRates {
  suspend fun execute(latitude: Double, longitude: Double): List<BestCourse>
}

class LoadExchangeRatesImpl @Inject constructor(
  private val locationRepository: LocationRepository,
  private val currencyRateRepository: CurrencyRateRepository
) : LoadExchangeRates {
  override suspend fun execute(latitude: Double, longitude: Double): List<BestCourse> {
    val city = locationRepository.resolveUserCity(latitude, longitude)
    return currencyRateRepository.loadExchangeRates(city, LocalDateTime.now())
  }
}