package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.repository.CurrencyRatesTimestampRepository
import java.time.LocalDateTime

class FakeCurrencyRatesTimestampRepository : CurrencyRatesTimestampRepository, Resettable {
  var isNeededToUpdateCurrencyRates = true
  var isSaveTimestampCalled = false

  override fun isNeededToUpdateCurrencyRates(now: LocalDateTime): Boolean =
    isNeededToUpdateCurrencyRates

  override fun saveTimestamp(now: LocalDateTime) {
    isSaveTimestampCalled = true
  }

  override fun reset() {
    isNeededToUpdateCurrencyRates = true
    isSaveTimestampCalled = false
  }
}
