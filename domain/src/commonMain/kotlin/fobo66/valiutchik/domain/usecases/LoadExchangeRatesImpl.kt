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

package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.LanguageTag
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.util.CURRENCY_NAME_EURO
import fobo66.valiutchik.core.util.CURRENCY_NAME_HRYVNIA
import fobo66.valiutchik.core.util.CURRENCY_NAME_RUBLE
import fobo66.valiutchik.core.util.CURRENCY_NAME_US_DOLLAR
import fobo66.valiutchik.core.util.CURRENCY_NAME_ZLOTY
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.DollarBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.EuroBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.HryvniaBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.RubleBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.ZlotyBuyRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

private data class CurrencyRatesIntermediate(
    val languageTag: LanguageTag,
    val rates: List<BestCourse>
)

class LoadExchangeRatesImpl(private val currencyRateRepository: CurrencyRateRepository) :
    LoadExchangeRates {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<BestCurrencyRate>> = currencyRateRepository.loadLocale()
        .combine(currencyRateRepository.loadExchangeRates(), ::CurrencyRatesIntermediate)
        .map { (languageTag, rates) ->
            rates.map {
                it.toRate(languageTag)
            }.filter {
                it.bank.isNotEmpty()
            }
        }

    private fun BestCourse.toRate(languageTag: LanguageTag): BestCurrencyRate {
        val bank = currencyRateRepository.formatBankName(this, languageTag)
        val rateValue = currencyRateRepository.formatRate(this, languageTag)

        return if (isBuy == true) {
            when (currencyName) {
                CURRENCY_NAME_US_DOLLAR -> DollarBuyRate(bank, rateValue)
                CURRENCY_NAME_EURO -> EuroBuyRate(bank, rateValue)
                CURRENCY_NAME_RUBLE -> RubleBuyRate(bank, rateValue)
                CURRENCY_NAME_ZLOTY -> ZlotyBuyRate(bank, rateValue)
                CURRENCY_NAME_HRYVNIA -> HryvniaBuyRate(bank, rateValue)
                else -> BestCurrencyRate.OtherBuyRate(bank, rateValue, "")
            }
        } else {
            when (currencyName) {
                CURRENCY_NAME_US_DOLLAR -> BestCurrencyRate.DollarSellRate(bank, rateValue)
                CURRENCY_NAME_EURO -> BestCurrencyRate.EuroSellRate(bank, rateValue)
                CURRENCY_NAME_RUBLE -> BestCurrencyRate.RubleSellRate(bank, rateValue)
                CURRENCY_NAME_ZLOTY -> BestCurrencyRate.ZlotySellRate(bank, rateValue)
                CURRENCY_NAME_HRYVNIA -> BestCurrencyRate.HryvniaSellRate(bank, rateValue)
                else -> BestCurrencyRate.OtherSellRate(bank, rateValue, "")
            }
        }
    }
}
