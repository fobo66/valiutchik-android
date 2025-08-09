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

import fobo66.valiutchik.api.entity.CurrencyRateSource
import fobo66.valiutchik.api.entity.CurrencyRatesRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

const val API_URL = "https://api.myfin.by/currency/rates"

private const val CLACKS_KEY = "X-Clacks-Overhead"
private const val CLACKS_VALUE = "GNU Terry Pratchett"

class CurrencyRatesDataSourceImpl(
    private val client: HttpClient,
    private val parser: CurrencyRatesResponseParser,
    private val ioDispatcher: CoroutineDispatcher
) : CurrencyRatesDataSource {

    private val apiCurrencies by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            CURRENCY_ALIAS_US_DOLLAR,
            CURRENCY_ALIAS_EURO,
            CURRENCY_ALIAS_HRYVNIA,
            CURRENCY_ALIAS_ZLOTY,
            CURRENCY_ALIAS_RUBLE
        )
    }

    override suspend fun loadExchangeRates(cityIndex: String): Map<Long, List<CurrencyRateSource>> =
        withContext(ioDispatcher) {
            try {
                apiCurrencies.map { CurrencyRatesRequest(cityId = cityIndex, currencyAlias = it) }
                    .map { request ->
                        async {
                            val response = client.post(API_URL) {
                                contentType(ContentType.Application.Json)
                                header(CLACKS_KEY, CLACKS_VALUE)
                                setBody(request)
                            }
                            parser.parse(response.bodyAsText())
                        }
                    }
                    .awaitAll()
                    .flatMap { it }
                    .groupBy { it.id }
            } catch (e: ResponseException) {
                throw IOException(e)
            }
        }
}
