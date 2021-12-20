package fobo66.exchangecourcesbelarus.model.repository

import java.time.LocalDateTime

interface CurrencyRatesTimestampRepository {
  fun isNeededToUpdateCurrencyRates(now: LocalDateTime): Boolean
  fun saveTimestamp(now: LocalDateTime)
}
