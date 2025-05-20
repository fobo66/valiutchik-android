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
 * Resolve currency buy rate based on the key
 */
fun Bank.resolveCurrencyBuyRate(name: CurrencyName): String = when (name) {
    CurrencyName.EUR -> eurBuy
    CurrencyName.RUB -> rubBuy
    CurrencyName.DOLLAR -> usdBuy
    CurrencyName.PLN -> plnBuy
    CurrencyName.UAH -> uahBuy
}

/**
 * Resolve currency sell rate based on the key
 */
fun Bank.resolveCurrencySellRate(name: CurrencyName): String = when (name) {
    CurrencyName.EUR -> eurSell
    CurrencyName.RUB -> rubSell
    CurrencyName.DOLLAR -> usdSell
    CurrencyName.PLN -> plnSell
    CurrencyName.UAH -> uahSell
}
