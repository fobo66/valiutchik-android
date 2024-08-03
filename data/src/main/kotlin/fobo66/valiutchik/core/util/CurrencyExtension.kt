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

package fobo66.valiutchik.core.util

import fobo66.valiutchik.api.entity.Currency

/**
 * Method to figure out which currency will be used depends on the context
 * By default, USD value is returned
 * If I find the better way to do it, I'll rewrite it.
 */
fun Currency.resolveCurrencyBuyRate(@CurrencyName name: String): String {
  return when (name) {
    EUR -> eurBuy
    RUB, RUR -> rurBuy
    USD -> usdBuy
    PLN -> plnBuy
    UAH -> uahBuy
    else -> usdBuy
  }
}

/**
 * Method to figure out which currency will be used depends on the context
 * By default, USD value is returned
 * If I find the better way to do it, I'll rewrite it.
 */
fun Currency.resolveCurrencySellRate(@CurrencyName name: String): String {
  return when (name) {
    EUR -> eurSell
    RUB, RUR -> rurSell
    USD -> usdSell
    PLN -> plnSell
    UAH -> uahSell
    else -> usdSell
  }
}
