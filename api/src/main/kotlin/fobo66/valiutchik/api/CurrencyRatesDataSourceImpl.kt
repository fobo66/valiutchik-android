/*
 *    Copyright 2023 Andrey Mukamolov
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

import fobo66.valiutchik.api.di.ApiPassword
import fobo66.valiutchik.api.di.ApiUsername
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.path
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.IOException
import javax.inject.Inject

const val BASE_URL = "https://admin.myfin.by/"

class CurrencyRatesDataSourceImpl @Inject constructor(
  private val client: HttpClient,
  private val parser: CurrencyRatesParser,
  @ApiUsername private val username: String,
  @ApiPassword private val password: String
) : CurrencyRatesDataSource {

  private val citiesMap: Map<String, String> by lazy {
    mapOf(
      "Минск" to "1",
      "Витебск" to "2",
      "Гомель" to "3",
      "Гродно" to "4",
      "Брест" to "5",
      "Могилёв" to "6"
    )
  }

  override suspend fun loadExchangeRates(city: String): Set<Currency> {
    val cityIndex = citiesMap[city] ?: "1"

    return try {
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
