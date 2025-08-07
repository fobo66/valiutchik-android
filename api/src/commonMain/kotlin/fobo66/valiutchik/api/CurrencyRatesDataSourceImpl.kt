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

import fobo66.valiutchik.api.entity.CurrencyRatesRequest
import fobo66.valiutchik.api.entity.CurrencyRatesResponse
import fobo66.valiutchik.api.entity.Mapobject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

const val BASE_URL = "https://api.myfin.by/"

private const val CLACKS_KEY = "X-Clacks-Overhead"
private const val CLACKS_VALUE = "GNU Terry Pratchett"

class CurrencyRatesDataSourceImpl(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher
) : CurrencyRatesDataSource {

    private val apiCurrencies by lazy(LazyThreadSafetyMode.NONE) {
        setOf("usd", "eur", "uah", "pln", "rub")
    }

    override suspend fun loadExchangeRates(cityIndex: String): Map<Long, List<Mapobject>> =
        withContext(ioDispatcher) {
            try {
                apiCurrencies.map { CurrencyRatesRequest(cityId = cityIndex, currencyAlias = it) }
                    .map { request ->
                        async {
                            client.post(BASE_URL) {
                                url {
                                    path("currency", "rates")
                                }
                                header(CLACKS_KEY, CLACKS_VALUE)
                                setBody(request)
                            }.body<CurrencyRatesResponse>()
                        }
                    }
                    .awaitAll()
                    .flatMap { it.mapobjects }
                    .groupBy { it.id }
            } catch (e: ResponseException) {
                throw IOException(e)
            }
        }
}
