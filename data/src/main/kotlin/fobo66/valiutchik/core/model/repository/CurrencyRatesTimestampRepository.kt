/*
 *    Copyright 2025 Andrey Mukamolov
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

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

/**
 * Repository to handle timestamps of currency rates refresh
 */
interface CurrencyRatesTimestampRepository {
    suspend fun isNeededToUpdateCurrencyRates(now: Instant, updateInterval: Float): Boolean
    suspend fun saveTimestamp(now: Instant)

    fun loadLatestTimestamp(now: Instant): Flow<Instant>
}
