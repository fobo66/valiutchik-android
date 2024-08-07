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

package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class CurrencyRatesInteractorImpl(
  private val refreshExchangeRates: RefreshExchangeRates,
  private val forceRefreshExchangeRates: ForceRefreshExchangeRates,
  private val refreshExchangeRatesForDefaultCity: RefreshExchangeRatesForDefaultCity,
  private val forceRefreshExchangeRatesForDefaultCity: ForceRefreshExchangeRatesForDefaultCity,
  private val loadExchangeRates: LoadExchangeRates,
) : CurrencyRatesInteractor {
  override suspend fun refreshExchangeRates() =
    refreshExchangeRates.execute(Clock.System.now())

  override suspend fun forceRefreshExchangeRates() =
    forceRefreshExchangeRates.execute(Clock.System.now())

  override suspend fun refreshExchangeRatesForDefaultCity() =
    refreshExchangeRatesForDefaultCity.execute(
      Clock.System.now()
    )

  override suspend fun forceRefreshExchangeRatesForDefaultCity() =
    forceRefreshExchangeRatesForDefaultCity.execute(
      Clock.System.now()
    )

  override fun loadExchangeRates(): Flow<List<BestCurrencyRate>> = loadExchangeRates.execute(
    Clock.System.now()
  )
}
