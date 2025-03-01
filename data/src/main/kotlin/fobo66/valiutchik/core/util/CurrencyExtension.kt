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
fun Bank.resolveCurrencyBuyRate(
  name: CurrencyNameNew,
): String =
  when (name) {
    CurrencyNameNew.EUR -> eurBuy
    CurrencyNameNew.RUB, CurrencyNameNew.RUR -> rubBuy
    CurrencyNameNew.USD -> usdBuy
    CurrencyNameNew.PLN -> plnBuy
    CurrencyNameNew.UAH -> uahBuy
  }

/**
 * Resolve currency sell rate based on the key
 */
fun Bank.resolveCurrencySellRate(
  name: CurrencyNameNew,
): String =
  when (name) {
    CurrencyNameNew.EUR -> eurSell
    CurrencyNameNew.RUB, CurrencyNameNew.RUR -> rubSell
    CurrencyNameNew.USD -> usdSell
    CurrencyNameNew.PLN -> plnSell
    CurrencyNameNew.UAH -> uahSell
  }
