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

package fobo66.valiutchik.domain.entities

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Entity that represents the best currency exchange rate
 * for consumption on the presentation layer
 */
@Immutable
data class BestCurrencyRate(
    val bank: String,
    @StringRes val currencyNameRes: Int,
    val currencyValue: String
)

@Stable
sealed class NewBestCurrencyRate(open val bank: String, open val rateValue: String) {
    @Immutable
    class DollarBuyRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class DollarSellRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class EuroBuyRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class EuroSellRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class HryvniaBuyRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class HryvniaSellRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class ZlotyBuyRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class ZlotySellRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class RubleBuyRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)

    @Immutable
    class RubleSellRate(bank: String, rateValue: String) : NewBestCurrencyRate(bank, rateValue)
}
