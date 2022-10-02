package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.entities.BestCourse
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow

interface CurrencyRateRepository {
  suspend fun refreshExchangeRates(city: String, now: LocalDateTime)
  fun loadExchangeRates(): Flow<List<BestCourse>>
}
