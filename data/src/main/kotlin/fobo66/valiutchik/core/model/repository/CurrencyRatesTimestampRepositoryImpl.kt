package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.TIMESTAMP
import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class CurrencyRatesTimestampRepositoryImpl @Inject constructor(
  private val preferencesDataSource: PreferencesDataSource
) : CurrencyRatesTimestampRepository {

  override suspend fun isNeededToUpdateCurrencyRates(
    now: LocalDateTime,
    updateInterval: Float
  ): Boolean {
    val timestamp = loadTimestamp(now)
    val cachedValueAge = Duration.between(timestamp, now)
    val interval: Duration = Duration.ofHours(
      updateInterval.toLong()
    )

    return cachedValueAge == Duration.ZERO || cachedValueAge > interval
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
}
