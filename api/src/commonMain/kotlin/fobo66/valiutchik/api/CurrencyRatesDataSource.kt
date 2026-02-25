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

package fobo66.valiutchik.api

import fobo66.valiutchik.api.entity.BankResponse
import fobo66.valiutchik.api.entity.CurrencyRateSource

/**
 * API datasource to load actual currency rates
 */
interface CurrencyRatesDataSource {

    /**
     * Load exchange rates for the provided currencies in the given city
     * @param currencies list of currency identifiers
     * @param cityIndex identifier of the city
     * @return rates for the currencies across the exchange offices
     */
    suspend fun loadExchangeRates(
        currencies: List<String>,
        cityIndex: Int
    ): Map<Long, List<CurrencyRateSource>>

    /**
     * Load info about banks
     * @return list of banks
     */
    suspend fun loadBanks(): List<BankResponse>
}
