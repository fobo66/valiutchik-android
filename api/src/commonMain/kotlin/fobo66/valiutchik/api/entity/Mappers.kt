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

package fobo66.valiutchik.api.entity

import androidx.collection.MutableScatterMap
import fobo66.valiutchik.api.TAG_NAME_BANK_ID
import fobo66.valiutchik.api.TAG_NAME_BANK_NAME
import fobo66.valiutchik.api.TAG_NAME_DATE
import fobo66.valiutchik.api.TAG_NAME_EUR_BUY
import fobo66.valiutchik.api.TAG_NAME_EUR_SELL
import fobo66.valiutchik.api.TAG_NAME_FILIAL_ID
import fobo66.valiutchik.api.TAG_NAME_PLN_BUY
import fobo66.valiutchik.api.TAG_NAME_PLN_SELL
import fobo66.valiutchik.api.TAG_NAME_RUR_BUY
import fobo66.valiutchik.api.TAG_NAME_RUR_SELL
import fobo66.valiutchik.api.TAG_NAME_UAH_BUY
import fobo66.valiutchik.api.TAG_NAME_UAH_SELL
import fobo66.valiutchik.api.TAG_NAME_USD_BUY
import fobo66.valiutchik.api.TAG_NAME_USD_SELL

/**
 * Builder for currency object
 */
fun MutableScatterMap<String, String>.toCurrency(): Bank = Bank(
    bankId = get(TAG_NAME_BANK_ID)?.toLongOrNull() ?: 0L,
    filialId = get(TAG_NAME_FILIAL_ID)?.toLongOrNull() ?: 0L,
    date = get(TAG_NAME_DATE).orEmpty(),
    bankName = get(TAG_NAME_BANK_NAME).orEmpty(),
    usdBuy = ExchangeRateValue(resolveRateValue(get(TAG_NAME_USD_BUY), UNDEFINED_BUY_RATE)),
    usdSell = ExchangeRateValue(
        resolveRateValue(get(TAG_NAME_USD_SELL), UNDEFINED_SELL_RATE)
    ),
    eurBuy = ExchangeRateValue(resolveRateValue(get(TAG_NAME_EUR_BUY), UNDEFINED_BUY_RATE)),
    eurSell = ExchangeRateValue(
        resolveRateValue(get(TAG_NAME_EUR_SELL), UNDEFINED_SELL_RATE)
    ),
    rubBuy = ExchangeRateValue(resolveRateValue(get(TAG_NAME_RUR_BUY), UNDEFINED_BUY_RATE)),
    rubSell = ExchangeRateValue(
        resolveRateValue(get(TAG_NAME_RUR_SELL), UNDEFINED_SELL_RATE)
    ),
    plnBuy = ExchangeRateValue(resolveRateValue(get(TAG_NAME_PLN_BUY), UNDEFINED_BUY_RATE)),
    plnSell = ExchangeRateValue(
        resolveRateValue(get(TAG_NAME_PLN_SELL), UNDEFINED_SELL_RATE)
    ),
    uahBuy = ExchangeRateValue(resolveRateValue(get(TAG_NAME_UAH_BUY), UNDEFINED_BUY_RATE)),
    uahSell = ExchangeRateValue(
        resolveRateValue(get(TAG_NAME_UAH_SELL), UNDEFINED_SELL_RATE)
    )
)

fun resolveRateValue(rawValue: String?, defaultValue: Double = UNDEFINED_RATE): Double =
    if (rawValue.isNullOrEmpty() || rawValue == UNKNOWN_RAW_RATE) {
        defaultValue
    } else {
        rawValue.toDouble()
    }
