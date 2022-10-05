package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.util.Resettable
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCurrencyRateRepository : CurrencyRateRepository, Resettable {
  var isRefreshed = false

  override suspend fun refreshExchangeRates(city: String, now: LocalDateTime) {
    isRefreshed = true
  }

  override fun loadExchangeRates(): Flow<List<BestCourse>> = flowOf(emptyList())

  override fun reset() {
    isRefreshed = false
  }
}
