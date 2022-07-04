package fobo66.exchangecourcesbelarus.model.repository

import java.time.LocalDateTime

interface CurrencyRatesTimestampRepository {
  suspend fun isNeededToUpdateCurrencyRates(now: LocalDateTime, updateInterval: Float): Boolean
  suspend fun saveTimestamp(now: LocalDateTime)
}
