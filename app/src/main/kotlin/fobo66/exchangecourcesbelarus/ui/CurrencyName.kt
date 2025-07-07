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

package fobo66.exchangecourcesbelarus.ui

import androidx.annotation.StringRes
import fobo66.exchangecourcesbelarus.R
import fobo66.valiutchik.domain.entities.BestCurrencyRate

@StringRes
fun BestCurrencyRate.resolveCurrencyName(): Int = when (this) {
    is BestCurrencyRate.DollarBuyRate -> R.string.currency_name_usd_buy
    is BestCurrencyRate.DollarSellRate -> R.string.currency_name_usd_sell
    is BestCurrencyRate.EuroBuyRate -> R.string.currency_name_eur_buy
    is BestCurrencyRate.EuroSellRate -> R.string.currency_name_eur_sell
    is BestCurrencyRate.HryvniaBuyRate -> R.string.currency_name_uah_buy
    is BestCurrencyRate.HryvniaSellRate -> R.string.currency_name_uah_sell
    is BestCurrencyRate.ZlotyBuyRate -> R.string.currency_name_pln_buy
    is BestCurrencyRate.ZlotySellRate -> R.string.currency_name_pln_sell
    is BestCurrencyRate.RubleBuyRate -> R.string.currency_name_rub_buy
    is BestCurrencyRate.RubleSellRate -> R.string.currency_name_rub_sell
}
