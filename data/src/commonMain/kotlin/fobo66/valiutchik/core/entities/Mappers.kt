/*
 *    Copyright 2026 Andrey Mukamolov
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

package fobo66.valiutchik.core.entities

import dev.fobo66.valiutchik.core.db.Bank
import dev.fobo66.valiutchik.core.db.Currency
import dev.fobo66.valiutchik.core.db.Rate
import fobo66.valiutchik.api.entity.BankResponse
import fobo66.valiutchik.api.entity.CurrencyRateSource
import fobo66.valiutchik.api.entity.CurrencyResponse
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun CurrencyRateSource.toRate(): Rate = Rate(
    id = id,
    date = resolveTimestamp().toString(),
    bankId = bankId,
    buyRate = currency.buy,
    sellRate = currency.sell,
    currencyId = currency.name,
    cityId = cityId
)

fun CurrencyRateSource.toBank(): Bank = Bank(
    id = bankId,
    name = bankName,
    formattedName = bankName
)

fun BankResponse.toBank(): Bank = Bank(
    id = id,
    name = fullname,
    formattedName = name
)

fun CurrencyResponse.toCurrency(): Currency = Currency(
    id = id,
    name = internationalName.lowercase(),
    symbol = symbol,
    multiplier = multiplier
)

@OptIn(ExperimentalTime::class)
private fun CurrencyRateSource.resolveTimestamp(): Instant = currency.dateUpdate.runCatching {
    Instant.fromEpochSeconds(this)
}.getOrDefault(Instant.DISTANT_PAST)
