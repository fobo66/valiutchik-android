/*
 *    Copyright 2024 Andrey Mukamolov
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

import fobo66.valiutchik.api.entity.Currency
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.path
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

const val BASE_URL = "https://admin.myfin.by/"

class CurrencyRatesDataSourceImpl(
  private val client: HttpClient,
  private val parser: CurrencyRatesParser,
  private val username: String,
  private val password: String,
  private val ioDispatcher: CoroutineDispatcher
) : CurrencyRatesDataSource {

  private val citiesMap: Map<String, String> by lazy {
    mapOf(
      "Minsk" to "1",
      "Vitsebsk" to "2",
      "Homyel" to "3",
      "Hrodna" to "4",
      "Brest" to "5",
      "Mahilyow" to "6"
    )
  }

  override suspend fun loadExchangeRates(city: String): Set<Currency> = withContext(ioDispatcher) {
    val cityIndex = citiesMap[city] ?: "1"

    try {
      val response = client.get(BASE_URL) {
        url {
          path("outer", "authXml", cityIndex)
        }
        basicAuth(username, password)
      }
      parser.parse(response.bodyAsChannel().toInputStream())
    } catch (e: ResponseException) {
      throw IOException(e)
    }
  }
}
