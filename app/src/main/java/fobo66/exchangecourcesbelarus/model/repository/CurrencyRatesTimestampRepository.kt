package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.TIMESTAMP
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class CurrencyRatesTimestampRepository @Inject constructor(
  private val preferencesDataSource: PreferencesDataSource
) {

  private val updateInterval: Duration by lazy {
    Duration.ofHours(
      preferencesDataSource.loadInt("update_interval", DEFAULT_UPDATE_INTERVAL).toLong()
    )
  }

  fun isNeededToUpdateCurrencyRates(now: LocalDateTime): Boolean {
    val timestamp = loadTimestamp(now)
    val cachedValueAge = Duration.between(timestamp, now)

    return cachedValueAge == Duration.ZERO || cachedValueAge > updateInterval
  }

  fun saveTimestamp(now: LocalDateTime) {
    val nowString = now.toString()
    preferencesDataSource.saveString(TIMESTAMP, nowString)
  }

  private fun loadTimestamp(fallbackTimestamp: LocalDateTime): LocalDateTime {
    val rawTimestamp: String = preferencesDataSource.loadString(TIMESTAMP)

    return if (rawTimestamp.isEmpty()) {
      fallbackTimestamp
    } else {
      LocalDateTime.parse(rawTimestamp)
    }
  }

  companion object {
    private const val DEFAULT_UPDATE_INTERVAL = 3
  }
}
