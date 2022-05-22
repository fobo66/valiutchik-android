package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.KEY_UPDATE_INTERVAL
import fobo66.valiutchik.core.TIMESTAMP
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class CurrencyRatesTimestampRepositoryImpl @Inject constructor(
  private val preferencesDataSource: PreferencesDataSource
) : CurrencyRatesTimestampRepository {

  override suspend fun isNeededToUpdateCurrencyRates(now: LocalDateTime): Boolean {
    val timestamp = loadTimestamp(now)
    val cachedValueAge = Duration.between(timestamp, now)
    val updateInterval: Duration = Duration.ofHours(
      preferencesDataSource.loadInt(KEY_UPDATE_INTERVAL, DEFAULT_UPDATE_INTERVAL).toLong()
    )

    return cachedValueAge == Duration.ZERO || cachedValueAge > updateInterval
  }

  override suspend fun saveTimestamp(now: LocalDateTime) {
    val nowString = now.toString()
    preferencesDataSource.saveString(TIMESTAMP, nowString)
  }

  private suspend fun loadTimestamp(fallbackTimestamp: LocalDateTime): LocalDateTime {
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
