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
import fobo66.valiutchik.api.entity.CurrencyRatesRequest
import fobo66.valiutchik.api.entity.CurrencyResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.readBuffer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

const val API_URL_RATES = "https://api.myfin.by/currency/rates"
const val API_URL_BANKS = "https://api.myfin.by/banks"
const val API_URL_CURRENCIES = "https://api.myfin.by/currency"

private const val CLACKS_KEY = "X-Clacks-Overhead"
private const val CLACKS_VALUE = "GNU Terry Pratchett"

class CurrencyRatesDataSourceImpl(
    private val client: HttpClient,
    private val parser: ApiResponseParser,
    private val ioDispatcher: CoroutineDispatcher
) : CurrencyRatesDataSource {
    override suspend fun loadExchangeRates(
        currencies: List<String>,
        cityIndex: Int
    ): Map<Long, List<CurrencyRateSource>> = withContext(ioDispatcher) {
        try {
            currencies.map { CurrencyRatesRequest(cityId = cityIndex, currencyAlias = it) }
                .map { request ->
                    async {
                        val response = client.post(API_URL_RATES) {
                            contentType(ContentType.Application.Json)
                            header(CLACKS_KEY, CLACKS_VALUE)
                            setBody(request)
                        }
                        parser.parseRates(response.bodyAsChannel().readBuffer())
                    }
                }
                .awaitAll()
                .flatten()
                .groupBy { it.id }
        } catch (e: ResponseException) {
            throw IOException(e)
        }
    }

    override suspend fun loadBanks(): List<BankResponse> = withContext(ioDispatcher) {
        val response = client.get(API_URL_BANKS) {
            contentType(ContentType.Application.Json)
            header(CLACKS_KEY, CLACKS_VALUE)
        }
        parser.parseBanks(response.bodyAsChannel().readBuffer())
    }

    override suspend fun loadCurrencies(): List<CurrencyResponse> = withContext(ioDispatcher) {
        val response = client.get(API_URL_CURRENCIES) {
            contentType(ContentType.Application.Json)
            header(CLACKS_KEY, CLACKS_VALUE)
        }
        parser.parseCurrencies(response.bodyAsChannel().readBuffer())
    }
}
