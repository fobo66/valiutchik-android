/*
 *    Copyright 2026 Andrey Mukamolov
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

package fobo66.valiutchik.api

import fobo66.valiutchik.api.entity.BankResponse
import fobo66.valiutchik.api.entity.CityResponse
import fobo66.valiutchik.api.entity.CurrencyRateSource
import fobo66.valiutchik.api.entity.CurrencyResponse
import kotlinx.io.Source

/**
 * Response parser for [MyFIN](myfin.by) datasets. The API returns JSON with HTML content type,
 * so we need to process it separately
 */
interface ApiResponseParser {
    /**
     * Parse rates JSON response from the API
     *
     * @param body response body
     *
     * @return set of bank branch info with the actual rate
     */
    fun parseRates(body: Source): Set<CurrencyRateSource>

    /**
     * Parse banks JSON response from the API
     *
     * @param body response body
     *
     * @return list of banks
     */
    fun parseBanks(body: Source): List<BankResponse>

    /**
     * Parse currencies JSON response from the API
     *
     * @param body response body
     *
     * @return list of currencies
     */
    fun parseCurrencies(body: Source): List<CurrencyResponse>

    /**
     * Parse cities JSON response from the API
     *
     * @param body response body
     *
     * @return list of cities
     */
    fun parseCities(body: Source): List<CityResponse>
}
