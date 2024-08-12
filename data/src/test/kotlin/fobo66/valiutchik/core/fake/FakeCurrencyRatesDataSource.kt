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

package fobo66.valiutchik.core.fake

import fobo66.valiutchik.api.CurrencyRatesDataSource
import fobo66.valiutchik.api.entity.Currency
import java.io.IOException

class FakeCurrencyRatesDataSource : CurrencyRatesDataSource {
  var isError = false
  override suspend fun loadExchangeRates(cityIndex: String): Set<Currency> =
    if (isError) {
      throw IOException("test")
    } else {
      setOf(Currency())
    }
}
