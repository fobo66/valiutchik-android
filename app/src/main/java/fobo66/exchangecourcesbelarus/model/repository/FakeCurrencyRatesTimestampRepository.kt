package fobo66.exchangecourcesbelarus.model.repository

import java.time.LocalDateTime

class FakeCurrencyRatesTimestampRepository : CurrencyRatesTimestampRepository {
  var isNeededToUpdateCurrencyRates = true
  var isSaveTimestampCalled = false

  override fun isNeededToUpdateCurrencyRates(now: LocalDateTime): Boolean =
    isNeededToUpdateCurrencyRates

  override fun saveTimestamp(now: LocalDateTime) {
    isSaveTimestampCalled = true
  }
}
