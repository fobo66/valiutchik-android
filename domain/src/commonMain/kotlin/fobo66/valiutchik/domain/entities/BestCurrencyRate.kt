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

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class BestCurrencyRate(open val bank: String, open val rateValue: String) {
    @Immutable
    data class DollarBuyRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class DollarSellRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class EuroBuyRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class EuroSellRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class HryvniaBuyRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class HryvniaSellRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class ZlotyBuyRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class ZlotySellRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class RubleBuyRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)

    @Immutable
    data class RubleSellRate(override val bank: String, override val rateValue: String) :
        BestCurrencyRate(bank, rateValue)
}
