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
import kotlin.math.log10
import kotlin.math.roundToInt
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
