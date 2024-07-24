/*
 *    Copyright 2024 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.TIMESTAMP
import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class CurrencyRatesTimestampRepositoryImpl(
  private val preferencesDataSource: PreferencesDataSource
) : CurrencyRatesTimestampRepository {

  override suspend fun isNeededToUpdateCurrencyRates(
    now: Instant,
    updateInterval: Float
  ): Boolean {
    val timestamp = loadTimestamp(now)
    val cachedValueAge = now - timestamp
    val interval: Duration = updateInterval.toLong().hours

    return cachedValueAge == Duration.ZERO || cachedValueAge > interval
  }

  override suspend fun saveTimestamp(now: Instant) {
    val nowString = now.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
    preferencesDataSource.saveString(TIMESTAMP, nowString)
  }

  override fun loadLatestTimestamp(now: Instant): Flow<Instant> {
    return preferencesDataSource.observeString(
      TIMESTAMP,
      now.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
    )
      .map { LocalDateTime.parse(it).toInstant(TimeZone.currentSystemDefault()) }
  }

  private suspend fun loadTimestamp(fallbackTimestamp: Instant): Instant {
    val rawTimestamp: String = preferencesDataSource.loadString(TIMESTAMP)

    return if (rawTimestamp.isEmpty()) {
      fallbackTimestamp
    } else {
      LocalDateTime.parse(rawTimestamp).toInstant(TimeZone.currentSystemDefault())
    }
  }
}
