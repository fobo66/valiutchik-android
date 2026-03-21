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

/**
 * Datasource to load actual currency rates from the data provider
 */
interface ApiDataSource {

    /**
     * Load exchange rates for the provided currencies in the given city
     * @param currencies list of currency identifiers
     * @param cityIndex identifier of the city
     * @return rates for the currencies across the exchange offices
     */
    suspend fun loadExchangeRates(
        currencies: List<String>,
        cityIndex: Int
    ): List<CurrencyRateSource>

    /**
     * Load info about banks
     * @return list of banks
     */
    suspend fun loadBanks(): List<BankResponse>

    /**
     * Load info about currencies
     * @return list of currencies
     */
    suspend fun loadCurrencies(): List<CurrencyResponse>

    /**
     * Load info about cities
     * @return list of cities
     */
    suspend fun loadCities(): List<CityResponse>
}
