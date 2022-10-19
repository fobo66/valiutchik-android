/*
 *    Copyright 2022 Andrey Mukamolov
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

import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRatesDataSourceImpl @Inject constructor(
  private val exchangeRatesApi: ExchangeRatesApi
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
      exchangeRatesApi.loadExchangeRates(cityIndex)
    } catch (e: HttpException) {
      throw IOException(e)
    }
  }
}
