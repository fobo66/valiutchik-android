package fobo66.valiutchik.domain.fake

import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCurrencyRateRepository : CurrencyRateRepository {
  var isRefreshed = false

  override suspend fun refreshExchangeRates(city: String, now: LocalDateTime) {
    isRefreshed = true
  }

  override fun loadExchangeRates(): Flow<List<BestCourse>> = flowOf(emptyList())
}
