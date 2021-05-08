package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.TIMESTAMP
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class CurrencyRatesTimestampRepository @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) {

  private val maxStalePeriod: Duration = Duration.ofHours(DEFAULT_MAX_STALE_PERIOD)

  fun isNeededToUpdateCurrencyRates(now: LocalDateTime): Boolean {
    val timestamp = loadTimestamp(now)
    val cachedValueAge = Duration.between(timestamp, now)

    return cachedValueAge == Duration.ZERO || cachedValueAge > maxStalePeriod
  }

  fun saveTimestamp(now: LocalDateTime) {
    val nowString = now.toString()
    preferencesDataSource.saveString(TIMESTAMP, nowString)
  }

  private fun loadTimestamp(fallbackTimestamp: LocalDateTime): LocalDateTime {
    val rawTimestamp: String = preferencesDataSource.loadSting(TIMESTAMP)

    return if (rawTimestamp.isEmpty()) {
      fallbackTimestamp
    } else {
      LocalDateTime.parse(rawTimestamp)
    }
  }

  companion object {
    private const val DEFAULT_MAX_STALE_PERIOD = 3L
  }
}
