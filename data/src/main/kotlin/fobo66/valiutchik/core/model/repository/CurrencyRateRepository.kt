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

import fobo66.valiutchik.core.entities.BestCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

/**
 * Repository to process exchange rates
 */
interface CurrencyRateRepository {
    /**
     * Refresh exchange rates for the given city
     */
    suspend fun refreshExchangeRates(city: String, now: Instant, defaultCity: String = city)

    /**
     * Load exchange rates from database or from network
     */
    fun loadExchangeRates(latestTimestamp: Instant): Flow<List<BestCourse>>

    /**
     * Format currency rate into human-readable form
     */
    fun formatRate(rate: BestCourse): String
}
