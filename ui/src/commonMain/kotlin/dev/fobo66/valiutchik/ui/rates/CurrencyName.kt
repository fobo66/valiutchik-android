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

package dev.fobo66.valiutchik.ui.rates

import fobo66.valiutchik.domain.entities.BestCurrencyRate
import org.jetbrains.compose.resources.StringResource
import valiutchik.ui.generated.resources.Res
import valiutchik.ui.generated.resources.currency_name_eur_buy
import valiutchik.ui.generated.resources.currency_name_eur_sell
import valiutchik.ui.generated.resources.currency_name_pln_buy
import valiutchik.ui.generated.resources.currency_name_pln_sell
import valiutchik.ui.generated.resources.currency_name_rub_buy
import valiutchik.ui.generated.resources.currency_name_rub_sell
import valiutchik.ui.generated.resources.currency_name_uah_buy
import valiutchik.ui.generated.resources.currency_name_uah_sell
import valiutchik.ui.generated.resources.currency_name_usd_buy
import valiutchik.ui.generated.resources.currency_name_usd_sell

fun BestCurrencyRate.resolveCurrencyName(): StringResource = when (this) {
    is BestCurrencyRate.DollarBuyRate -> Res.string.currency_name_usd_buy
    is BestCurrencyRate.DollarSellRate -> Res.string.currency_name_usd_sell
    is BestCurrencyRate.EuroBuyRate -> Res.string.currency_name_eur_buy
    is BestCurrencyRate.EuroSellRate -> Res.string.currency_name_eur_sell
    is BestCurrencyRate.HryvniaBuyRate -> Res.string.currency_name_uah_buy
    is BestCurrencyRate.HryvniaSellRate -> Res.string.currency_name_uah_sell
    is BestCurrencyRate.ZlotyBuyRate -> Res.string.currency_name_pln_buy
    is BestCurrencyRate.ZlotySellRate -> Res.string.currency_name_pln_sell
    is BestCurrencyRate.RubleBuyRate -> Res.string.currency_name_rub_buy
    is BestCurrencyRate.RubleSellRate -> Res.string.currency_name_rub_sell
}
