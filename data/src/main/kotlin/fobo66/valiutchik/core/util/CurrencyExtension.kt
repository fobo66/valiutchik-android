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

package fobo66.valiutchik.core.util

import fobo66.valiutchik.api.entity.Bank

/**
 * Method to figure out which currency will be used depends on the context
 * By default, empty string is returned
 */
fun Bank.resolveCurrencyBuyRate(@CurrencyName name: String): String = when (name) {
    EUR -> eurBuy
    RUB, RUR -> rubBuy
    USD -> usdBuy
    PLN -> plnBuy
    UAH -> uahBuy
    else -> ""
}

/**
 * Method to figure out which currency will be used depends on the context
 * By default, empty string is returned
 */
fun Bank.resolveCurrencySellRate(@CurrencyName name: String): String = when (name) {
    EUR -> eurSell
    RUB, RUR -> rubSell
    USD -> usdSell
    PLN -> plnSell
    UAH -> uahSell
    else -> ""
}
