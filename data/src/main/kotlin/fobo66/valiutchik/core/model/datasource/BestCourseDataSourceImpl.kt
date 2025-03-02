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

package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.core.UNKNOWN_COURSE
import fobo66.valiutchik.core.util.CurrencyName
import fobo66.valiutchik.core.util.resolveCurrencyBuyRate
import fobo66.valiutchik.core.util.resolveCurrencySellRate
import java.util.EnumMap
import java.util.EnumSet

class BestCourseDataSourceImpl : BestCourseDataSource {
  private val currencyKeys: EnumSet<CurrencyName> by lazy(LazyThreadSafetyMode.NONE) {
    EnumSet.allOf(CurrencyName::class.java)
  }

  override fun findBestBuyCurrencies(courses: Set<Bank>): EnumMap<CurrencyName, Bank> =
    EnumMap<CurrencyName, Bank>(currencyKeys.associateWith { currencyKey ->
      courses
        .asSequence()
        .filter { isBuyRateCorrect(it, currencyKey) }
        .maxByOrNull { it.resolveCurrencyBuyRate(currencyKey) } ?: courses.first {
        isBuyRateCorrect(it, currencyKey)
      }
    })

  override fun findBestSellCurrencies(courses: Set<Bank>): EnumMap<CurrencyName, Bank> =
    EnumMap<CurrencyName, Bank>(currencyKeys.associateWith { currencyKey ->
      courses
        .asSequence()
        .filter { isSellRateCorrect(it, currencyKey) }
        .minByOrNull { it.resolveCurrencySellRate(currencyKey) } ?: courses.first {
        isSellRateCorrect(it, currencyKey)
      }
    })

  private fun isSellRateCorrect(
    currency: Bank,
    currencyKey: CurrencyName,
  ): Boolean {
    val rate = currency.resolveCurrencySellRate(currencyKey)
    return rate.isNotEmpty() && rate != UNKNOWN_COURSE
  }

  private fun isBuyRateCorrect(
    currency: Bank,
    currencyKey: CurrencyName,
  ): Boolean {
    val rate = currency.resolveCurrencyBuyRate(currencyKey)
    return rate.isNotEmpty() && rate != UNKNOWN_COURSE
  }
}
