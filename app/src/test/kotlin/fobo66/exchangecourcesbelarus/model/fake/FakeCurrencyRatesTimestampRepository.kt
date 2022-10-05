package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.repository.CurrencyRatesTimestampRepository
import fobo66.valiutchik.core.util.Resettable
import java.time.LocalDateTime

class FakeCurrencyRatesTimestampRepository : CurrencyRatesTimestampRepository, Resettable {
  var isNeededToUpdateCurrencyRates = true
  var isSaveTimestampCalled = false

  override suspend fun isNeededToUpdateCurrencyRates(now: LocalDateTime, updateInterval: Float): Boolean =
    isNeededToUpdateCurrencyRates

  override suspend fun saveTimestamp(now: LocalDateTime) {
    isSaveTimestampCalled = true
  }

  override fun reset() {
    isNeededToUpdateCurrencyRates = true
    isSaveTimestampCalled = false
  }
}
