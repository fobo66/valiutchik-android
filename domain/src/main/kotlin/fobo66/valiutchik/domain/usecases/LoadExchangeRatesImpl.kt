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

import android.icu.number.NumberFormatter
import android.icu.util.Currency
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.StringRes
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.model.repository.CurrencyRatesTimestampRepository
import fobo66.valiutchik.core.util.CurrencyName
import fobo66.valiutchik.core.util.EUR
import fobo66.valiutchik.core.util.PLN
import fobo66.valiutchik.core.util.RUB
import fobo66.valiutchik.core.util.RUR
import fobo66.valiutchik.core.util.UAH
import fobo66.valiutchik.core.util.USD
import fobo66.valiutchik.domain.R
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import java.util.Locale

class LoadExchangeRatesImpl(
  private val currencyRateRepository: CurrencyRateRepository,
  private val currencyRatesTimestampRepository: CurrencyRatesTimestampRepository,
) : LoadExchangeRates {
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun execute(now: Instant): Flow<List<BestCurrencyRate>> =
    currencyRatesTimestampRepository
      .loadLatestTimestamp(now)
      .flatMapLatest { currencyRateRepository.loadExchangeRates(it) }
      .map {
        it.map { bestCourse ->
          @StringRes val currencyNameRes =
            resolveCurrencyName(bestCourse.currencyName, bestCourse.isBuy)

          bestCourse.toBestCurrencyRate(
            currencyNameRes,
            formatCurrencyValue(bestCourse.currencyValue),
          )
        }
      }

  @StringRes
  private fun resolveCurrencyName(
    @CurrencyName currencyName: String,
    isBuy: Boolean,
  ) = when (currencyName to isBuy) {
    USD to true -> R.string.currency_name_usd_buy
    USD to false -> R.string.currency_name_usd_sell
    EUR to true -> R.string.currency_name_eur_buy
    EUR to false -> R.string.currency_name_eur_sell
    RUB to true, RUR to true -> R.string.currency_name_rub_buy
    RUB to false, RUR to false -> R.string.currency_name_rub_sell
    PLN to true -> R.string.currency_name_pln_buy
    PLN to false -> R.string.currency_name_pln_sell
    UAH to true -> R.string.currency_name_uah_buy
    UAH to false -> R.string.currency_name_uah_sell
    else -> 0
  }

  private fun formatCurrencyValue(rawValue: String): String =
    if (VERSION.SDK_INT >= VERSION_CODES.R) {
      NumberFormatter
        .withLocale(Locale.getDefault())
        .unit(Currency.getInstance("BYN"))
        .format(rawValue.toDoubleOrNull() ?: 0.0)
        .toString()
    } else {
      rawValue
    }

  private fun BestCourse.toBestCurrencyRate(
    @StringRes currencyNameRes: Int,
    currencyValue: String? = null,
  ): BestCurrencyRate =
    BestCurrencyRate(id, bank, currencyNameRes, currencyValue ?: this.currencyValue)
}
