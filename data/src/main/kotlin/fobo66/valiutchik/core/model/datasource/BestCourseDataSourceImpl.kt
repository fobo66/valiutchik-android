/*
 *    Copyright 2022 Andrey Mukamolov
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

package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.api.Currency
import fobo66.valiutchik.core.UNKNOWN_COURSE
import fobo66.valiutchik.core.util.EUR
import fobo66.valiutchik.core.util.RUB
import fobo66.valiutchik.core.util.USD
import fobo66.valiutchik.core.util.resolveCurrencyBuyRate
import fobo66.valiutchik.core.util.resolveCurrencySellRate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Algorithm for finding best currency rate for each currency
 * Created by fobo66 on 05.02.2017.
 */
@Singleton
class BestCourseDataSourceImpl @Inject constructor() : BestCourseDataSource {

  private val currencyKeys by lazy { listOf(USD, EUR, RUB) }

  override fun findBestBuyCurrencies(
    courses: Set<Currency>
  ): Map<String, Currency> = currencyKeys.associateWith { currencyKey ->
    courses.asSequence()
      .filter { isBuyRateCorrect(it, currencyKey) }
      .maxByOrNull { it.resolveCurrencyBuyRate(currencyKey) } ?: courses.first {
      isBuyRateCorrect(it, currencyKey)
    }
  }

  override fun findBestSellCurrencies(
    courses: Set<Currency>
  ): Map<String, Currency> = currencyKeys.associateWith { currencyKey ->
    courses.asSequence()
      .filter { isSellRateCorrect(it, currencyKey) }
      .minByOrNull { it.resolveCurrencySellRate(currencyKey) } ?: courses.first {
      isSellRateCorrect(it, currencyKey)
    }
  }

  private fun isSellRateCorrect(
    currency: Currency,
    currencyKey: String
  ) = currency.resolveCurrencySellRate(currencyKey) != UNKNOWN_COURSE

  private fun isBuyRateCorrect(
    currency: Currency,
    currencyKey: String
  ) = currency.resolveCurrencyBuyRate(currencyKey) != UNKNOWN_COURSE
}
