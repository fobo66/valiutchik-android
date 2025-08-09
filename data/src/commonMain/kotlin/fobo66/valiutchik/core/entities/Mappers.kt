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

import fobo66.valiutchik.api.CURRENCY_ALIAS_EURO
import fobo66.valiutchik.api.CURRENCY_ALIAS_HRYVNIA
import fobo66.valiutchik.api.CURRENCY_ALIAS_RUBLE
import fobo66.valiutchik.api.CURRENCY_ALIAS_US_DOLLAR
import fobo66.valiutchik.api.CURRENCY_ALIAS_ZLOTY
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.CurrencyRateSource
import fobo66.valiutchik.api.entity.resolveBuyRate
import fobo66.valiutchik.api.entity.resolveSellRate
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat

private const val UNDEFINED_DATE = "1970-01-01"

fun Bank.toRate(dateFormat: DateTimeFormat<LocalDate>): Rate = Rate(
    id = concatIds(bankId, filialId),
    date = resolveDate(date, dateFormat),
    bankName = bankName,
    usdBuy = usdBuy,
    usdSell = usdSell,
    eurBuy = eurBuy,
    eurSell = eurSell,
    rubBuy = rubBuy,
    rubSell = rubSell,
    plnBuy = plnBuy,
    plnSell = plnSell,
    uahBuy = uahBuy,
    uahSell = uahSell
)

@OptIn(ExperimentalTime::class)
fun List<CurrencyRateSource>.toRate(): Rate = Rate(
    id = concatIds(first().bankId, first().id),
    date = Instant.fromEpochSeconds(maxBy { it.currency.dateUpdate }.currency.dateUpdate)
        .toString(),
    bankName = first().bankName,
    usdBuy = resolveBuyRate(CURRENCY_ALIAS_US_DOLLAR),
    usdSell = resolveSellRate(CURRENCY_ALIAS_US_DOLLAR),
    eurBuy = resolveBuyRate(CURRENCY_ALIAS_EURO),
    eurSell = resolveSellRate(CURRENCY_ALIAS_EURO),
    rubBuy = resolveBuyRate(CURRENCY_ALIAS_RUBLE),
    rubSell = resolveSellRate(CURRENCY_ALIAS_RUBLE),
    plnBuy = resolveBuyRate(CURRENCY_ALIAS_ZLOTY),
    plnSell = resolveSellRate(CURRENCY_ALIAS_ZLOTY),
    uahBuy = resolveBuyRate(CURRENCY_ALIAS_HRYVNIA),
    uahSell = resolveSellRate(CURRENCY_ALIAS_HRYVNIA)
)

private fun resolveDate(rawDate: String, format: DateTimeFormat<LocalDate>): String =
    if (rawDate.isEmpty()) {
        UNDEFINED_DATE
    } else {
        LocalDate.parse(rawDate, format).toString()
    }

private fun concatIds(primary: Long, secondary: Long): Long {
    val secondaryLength = if (secondary == 0L) {
        1
    } else {
        (log10(secondary.toFloat()) + 1).roundToInt()
    }
    val multiplier = 10L.pow(secondaryLength)
    return (multiplier * primary) + secondary
}

private fun Long.pow(exponent: Int): Long {
    var result = 1L
    repeat(exponent) {
        result *= this
    }
    return result
}
