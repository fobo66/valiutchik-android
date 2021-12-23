package fobo66.exchangecourcesbelarus.model.repository

import java.time.LocalDateTime

interface CurrencyRatesTimestampRepository {
  suspend fun isNeededToUpdateCurrencyRates(now: LocalDateTime): Boolean
  suspend fun saveTimestamp(now: LocalDateTime)
}
