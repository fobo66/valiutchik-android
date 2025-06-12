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

import androidx.annotation.StringRes
import androidx.collection.ScatterMap
import androidx.collection.mutableScatterMapOf
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.util.CurrencyName
import fobo66.valiutchik.domain.R
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlin.LazyThreadSafetyMode.NONE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoadExchangeRatesImpl(private val currencyRateRepository: CurrencyRateRepository) :
    LoadExchangeRates {
    private val buyLabels: ScatterMap<CurrencyName, Int> by lazy(NONE) {
        mutableScatterMapOf(
            CurrencyName.DOLLAR to R.string.currency_name_usd_buy,
            CurrencyName.EUR to R.string.currency_name_eur_buy,
            CurrencyName.RUB to R.string.currency_name_rub_buy,
            CurrencyName.PLN to R.string.currency_name_pln_buy,
            CurrencyName.UAH to R.string.currency_name_uah_buy
        )
    }

    private val sellLabels: ScatterMap<CurrencyName, Int> by lazy(NONE) {
        mutableScatterMapOf(
            CurrencyName.DOLLAR to R.string.currency_name_usd_sell,
            CurrencyName.EUR to R.string.currency_name_eur_sell,
            CurrencyName.RUB to R.string.currency_name_rub_sell,
            CurrencyName.PLN to R.string.currency_name_pln_sell,
            CurrencyName.UAH to R.string.currency_name_uah_sell
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<BestCurrencyRate>> =
        currencyRateRepository.loadExchangeRates()
            .map {
                it.map { bestCourse ->
                    @StringRes val currencyNameRes =
                        resolveCurrencyName(bestCourse.currencyName, bestCourse.isBuy)

                    BestCurrencyRate(
                        bank = bestCourse.bank,
                        currencyNameRes = currencyNameRes,
                        currencyValue = currencyRateRepository.formatRate(bestCourse)
                    )
                }
            }

    @StringRes
    private fun resolveCurrencyName(currencyName: CurrencyName, isBuy: Boolean): Int {
        val labelRes =
            if (isBuy) {
                buyLabels[currencyName]
            } else {
                sellLabels[currencyName]
            }

        return labelRes ?: 0
    }
}
