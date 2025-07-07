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
import fobo66.valiutchik.domain.entities.NewBestCurrencyRate

@StringRes
fun NewBestCurrencyRate.resolveCurrencyName(): Int = when (this) {
    is NewBestCurrencyRate.DollarBuyRate -> R.string.currency_name_usd_buy
    is NewBestCurrencyRate.DollarSellRate -> R.string.currency_name_usd_sell
    is NewBestCurrencyRate.EuroBuyRate -> R.string.currency_name_eur_buy
    is NewBestCurrencyRate.EuroSellRate -> R.string.currency_name_eur_sell
    is NewBestCurrencyRate.HryvniaBuyRate -> R.string.currency_name_uah_buy
    is NewBestCurrencyRate.HryvniaSellRate -> R.string.currency_name_uah_sell
    is NewBestCurrencyRate.ZlotyBuyRate -> R.string.currency_name_pln_buy
    is NewBestCurrencyRate.ZlotySellRate -> R.string.currency_name_pln_sell
    is NewBestCurrencyRate.RubleBuyRate -> R.string.currency_name_rub_buy
    is NewBestCurrencyRate.RubleSellRate -> R.string.currency_name_rub_sell
}
