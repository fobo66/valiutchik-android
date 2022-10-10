package fobo66.valiutchik.core.model.repository

import java.time.LocalDateTime

interface CurrencyRatesTimestampRepository {
  suspend fun isNeededToUpdateCurrencyRates(now: LocalDateTime, updateInterval: Float): Boolean
  suspend fun saveTimestamp(now: LocalDateTime)
}
