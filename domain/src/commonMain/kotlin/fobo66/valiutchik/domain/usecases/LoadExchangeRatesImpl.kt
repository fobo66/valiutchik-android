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
import fobo66.valiutchik.core.util.CurrencyName
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.DollarBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.EuroBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.HryvniaBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.RubleBuyRate
import fobo66.valiutchik.domain.entities.BestCurrencyRate.ZlotyBuyRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

private data class CurrencyRatesIntermediate(val rates: List<BestCourse>, val locale: LanguageTag)

class LoadExchangeRatesImpl(private val currencyRateRepository: CurrencyRateRepository) :
    LoadExchangeRates {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<BestCurrencyRate>> =
        currencyRateRepository.loadExchangeRates()
            .zip(currencyRateRepository.loadLocale(), ::CurrencyRatesIntermediate)
            .map { (rates, languageTag) ->
                rates.map {
                    it.toRate(languageTag)
                }.filter {
                    it.bank.isNotEmpty()
                }
            }

    private fun BestCourse.toRate(languageTag: LanguageTag): BestCurrencyRate {
        val bank = currencyRateRepository.formatBankName(this, languageTag)
        val rateValue = currencyRateRepository.formatRate(this, languageTag)
        val currency = requireNotNull(currencyName) {
            "Null currency name should not happen here!"
        }

        return if (isBuy == true) {
            when (currency) {
                CurrencyName.DOLLAR -> DollarBuyRate(bank, rateValue)
                CurrencyName.EUR -> EuroBuyRate(bank, rateValue)
                CurrencyName.RUB -> RubleBuyRate(bank, rateValue)
                CurrencyName.PLN -> ZlotyBuyRate(bank, rateValue)
                CurrencyName.UAH -> HryvniaBuyRate(bank, rateValue)
            }
        } else {
            when (currency) {
                CurrencyName.DOLLAR -> BestCurrencyRate.DollarSellRate(bank, rateValue)
                CurrencyName.EUR -> BestCurrencyRate.EuroSellRate(bank, rateValue)
                CurrencyName.RUB -> BestCurrencyRate.RubleSellRate(bank, rateValue)
                CurrencyName.PLN -> BestCurrencyRate.ZlotySellRate(bank, rateValue)
                CurrencyName.UAH -> BestCurrencyRate.HryvniaSellRate(bank, rateValue)
            }
        }
    }
}
