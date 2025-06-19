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

import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.Banks
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.path
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

const val BASE_URL = "https://admin.myfin.by/"

private const val CLACKS_KEY = "X-Clacks-Overhead"
private const val CLACKS_VALUE = "GNU Terry Pratchett"

class CurrencyRatesDataSourceImpl(
    private val client: HttpClient,
    private val username: String,
    private val password: String,
    private val ioDispatcher: CoroutineDispatcher
) : CurrencyRatesDataSource {
    override suspend fun loadExchangeRates(cityIndex: String): Set<Bank> =
        withContext(ioDispatcher) {
            try {
                val response =
                    client.get(BASE_URL) {
                        url {
                            path("outer", "authXml", cityIndex)
                        }
                        basicAuth(username, password)
                        header(CLACKS_KEY, CLACKS_VALUE)
                    }

                response.body<Banks>().banks
            } catch (e: ResponseException) {
                throw IOException(e)
            }
        }
}
