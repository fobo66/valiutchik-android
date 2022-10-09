package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.entities.BestCourse
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow

interface CurrencyRateRepository {
  suspend fun refreshExchangeRates(city: String, now: LocalDateTime)
  fun loadExchangeRates(): Flow<List<BestCourse>>
}
