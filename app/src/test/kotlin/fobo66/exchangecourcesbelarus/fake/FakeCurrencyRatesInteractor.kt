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

package fobo66.exchangecourcesbelarus.fake

import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.usecases.CurrencyRatesInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCurrencyRatesInteractor : CurrencyRatesInteractor {
  val bestCourses = MutableStateFlow(emptyList<BestCurrencyRate>())

  var isRefreshed = false
  var isForceRefreshed = false
  var isDefaultRefreshed = false
  var isDefaultForceRefreshed = false

  override suspend fun refreshExchangeRates() {
    isRefreshed = true
  }

  override suspend fun forceRefreshExchangeRates() {
    isForceRefreshed = true
  }

  override suspend fun refreshExchangeRatesForDefaultCity() {
    isDefaultRefreshed = true
  }

  override suspend fun forceRefreshExchangeRatesForDefaultCity() {
    isDefaultForceRefreshed = true
  }

  override fun loadExchangeRates(): Flow<List<BestCurrencyRate>> = bestCourses
}
