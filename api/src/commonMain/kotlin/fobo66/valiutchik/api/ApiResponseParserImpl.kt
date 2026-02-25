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
import fobo66.valiutchik.api.entity.CurrencyRatesResponse
import fobo66.valiutchik.api.entity.CurrencyResponse
import kotlinx.io.Source
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.decodeFromSource

class ApiResponseParserImpl(private val json: Json) : ApiResponseParser {
    @OptIn(ExperimentalSerializationApi::class)
    override fun parseRates(body: Source): Set<CurrencyRateSource> {
        val response = json.decodeFromSource<CurrencyRatesResponse>(body)

        return response.results
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun parseBanks(body: Source): List<BankResponse> = json.decodeFromSource(body)

    @OptIn(ExperimentalSerializationApi::class)
    override fun parseCurrencies(body: Source): List<CurrencyResponse> = json.decodeFromSource(body)
}
