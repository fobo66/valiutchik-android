/*
 *    Copyright 2022 Andrey Mukamolov
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
import java.time.Duration
import java.time.LocalDateTime
import org.koin.core.annotation.Single

@Single
class CurrencyRatesTimestampRepositoryImpl(
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
