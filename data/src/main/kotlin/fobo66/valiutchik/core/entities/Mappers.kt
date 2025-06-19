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

package fobo66.valiutchik.core.entities

import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.ExchangeRateValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat

const val UNDEFINED_BUY_RATE = 0.0
const val UNDEFINED_SELL_RATE = 999.0

private const val UNDEFINED_DATE = "1970-01-01"

fun Bank.toRate(dateFormat: DateTimeFormat<LocalDate>, formattedBankName: String): Rate = Rate(
    id = (bankId + filialId).toLongOrNull() ?: 0L,
    date = resolveDate(date, dateFormat),
    bankName = formattedBankName,
    usdBuy = resolveRate(usdBuy, UNDEFINED_BUY_RATE),
    usdSell = resolveRate(usdSell, UNDEFINED_SELL_RATE),
    eurBuy = resolveRate(eurBuy, UNDEFINED_BUY_RATE),
    eurSell = resolveRate(eurSell, UNDEFINED_SELL_RATE),
    rubBuy = resolveRate(rubBuy, UNDEFINED_BUY_RATE),
    rubSell = resolveRate(rubSell, UNDEFINED_SELL_RATE),
    plnBuy = resolveRate(plnBuy, UNDEFINED_BUY_RATE),
    plnSell = resolveRate(plnSell, UNDEFINED_SELL_RATE),
    uahBuy = resolveRate(uahBuy, UNDEFINED_BUY_RATE),
    uahSell = resolveRate(uahSell, UNDEFINED_SELL_RATE)
)

private fun resolveRate(rate: ExchangeRateValue, defaultValue: Double): Double =
    if (rate.rate != -1.0) {
        rate.rate
    } else {
        defaultValue
    }

private fun resolveDate(rawDate: String, format: DateTimeFormat<LocalDate>): String =
    if (rawDate.isEmpty()) {
        UNDEFINED_DATE
    } else {
        LocalDate.parse(rawDate, format).toString()
    }
