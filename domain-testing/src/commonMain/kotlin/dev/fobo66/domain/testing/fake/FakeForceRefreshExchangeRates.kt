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

package dev.fobo66.domain.testing.fake

import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRates

class FakeForceRefreshExchangeRates : ForceRefreshExchangeRates {
    var isRefreshed: Boolean = false

    var error: Boolean = false

    override suspend fun execute() = if (error) {
        throw CurrencyRatesLoadFailedException(Throwable("test"))
    } else {
        isRefreshed = true
    }
}
