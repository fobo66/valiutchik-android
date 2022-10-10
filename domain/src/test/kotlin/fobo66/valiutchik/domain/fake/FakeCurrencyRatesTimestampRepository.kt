package fobo66.valiutchik.domain.fake

import fobo66.valiutchik.core.model.repository.CurrencyRatesTimestampRepository
import java.time.LocalDateTime

class FakeCurrencyRatesTimestampRepository : CurrencyRatesTimestampRepository {
  var isNeededToUpdateCurrencyRates = true
  var isSaveTimestampCalled = false

  override suspend fun isNeededToUpdateCurrencyRates(
    now: LocalDateTime,
    updateInterval: Float
  ): Boolean =
    isNeededToUpdateCurrencyRates

  override suspend fun saveTimestamp(now: LocalDateTime) {
    isSaveTimestampCalled = true
  }
}
