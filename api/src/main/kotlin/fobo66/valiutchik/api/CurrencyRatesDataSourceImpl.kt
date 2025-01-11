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

import fobo66.valiutchik.api.entity.Banks
import fobo66.valiutchik.api.entity.Currency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException

const val BASE_URL = "https://admin.myfin.by/"

class CurrencyRatesDataSourceImpl(
  private val client: HttpClient,
  private val username: String,
  private val password: String,
  private val ioDispatcher: CoroutineDispatcher,
) : CurrencyRatesDataSource {
  override suspend fun loadExchangeRates(cityIndex: String): Set<Currency> =
    withContext(ioDispatcher) {
      try {
        val response =
          client.get(BASE_URL) {
            url {
              path("outer", "authXml", cityIndex)
            }
            basicAuth(username, password)
          }

        response
          .body<Banks>()
          .banks
          .map {
            Currency(
              bankname = it.bankName,
              usdBuy = it.usdBuy,
              usdSell = it.usdSell,
              eurBuy = it.eurBuy,
              eurSell = it.eurSell,
              rurBuy = it.rubBuy,
              rurSell = it.rubSell,
              plnBuy = it.plnBuy,
              plnSell = it.plnSell,
              uahBuy = it.uahBuy,
              uahSell = it.uahSell,
            )
          }.toSet()
      } catch (e: ResponseException) {
        throw IOException(e)
      }
    }
}
