/*
 *    Copyright 2026 Andrey Mukamolov
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
import fobo66.valiutchik.core.model.repository.LocaleRepository
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

private data class CurrencyRatesIntermediate(
    val languageTag: LanguageTag,
    val rates: List<BestCourse>
)

class LoadExchangeRatesImpl(
    private val currencyRateRepository: CurrencyRateRepository,
    private val localeRepository: LocaleRepository
) : LoadExchangeRates {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<BestCurrencyRate>> = localeRepository.loadLocale()
        .combine(currencyRateRepository.loadExchangeRates(), ::CurrencyRatesIntermediate)
        .map { (languageTag, rates) ->
            rates.map {
                it.toRate(languageTag)
            }
        }

    private fun BestCourse.toRate(languageTag: LanguageTag): BestCurrencyRate {
        val key = currencyId * 10 + (if (isBuy == true) 1 else 0)
        val bank = localeRepository.formatBankName(this, languageTag)
        val rateValue = localeRepository.formatRate(this, languageTag)
        val name = localeRepository.formatCurrencyName(this, languageTag)
        val symbol = localeRepository.formatCurrencySymbol(this, languageTag)

        return if (isBuy == true) {
            BestCurrencyRate.BuyRate(key, bank, rateValue, multiplier, name, symbol)
        } else {
            BestCurrencyRate.SellRate(key, bank, rateValue, multiplier, name, symbol)
        }
    }
}
