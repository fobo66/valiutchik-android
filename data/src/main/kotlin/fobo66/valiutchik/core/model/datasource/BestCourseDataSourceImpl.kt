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

import androidx.collection.ScatterMap
import androidx.collection.ScatterSet
import androidx.collection.mutableScatterMapOf
import androidx.collection.scatterSetOf
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.core.UNKNOWN_COURSE
import fobo66.valiutchik.core.util.CurrencyName
import fobo66.valiutchik.core.util.resolveCurrencyBuyRate
import fobo66.valiutchik.core.util.resolveCurrencySellRate

class BestCourseDataSourceImpl : BestCourseDataSource {
  private val currencyKeys: ScatterSet<CurrencyName> by lazy(LazyThreadSafetyMode.NONE) {
    scatterSetOf(
      CurrencyName.DOLLAR,
      CurrencyName.EUR,
      CurrencyName.RUB,
      CurrencyName.PLN,
      CurrencyName.UAH,
    )
  }

  override fun findBestBuyCurrencies(courses: Set<Bank>): Map<CurrencyName, Bank> =
    currencyKeys
      .associateWith { currencyKey ->
        courses
          .asSequence()
          .filter { isBuyRateCorrect(it, currencyKey) }
          .maxByOrNull { it.resolveCurrencyBuyRate(currencyKey) } ?: courses.first {
          isBuyRateCorrect(it, currencyKey)
        }
      }.asMap()

  override fun findBestSellCurrencies(courses: Set<Bank>): Map<CurrencyName, Bank> =
    currencyKeys
      .associateWith { currencyKey ->
        courses
          .asSequence()
          .filter { isSellRateCorrect(it, currencyKey) }
          .minByOrNull { it.resolveCurrencySellRate(currencyKey) } ?: courses.first {
          isSellRateCorrect(it, currencyKey)
        }
      }.asMap()

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

  private inline fun <K, V> ScatterSet<K>.associateWith(valueSelector: (K) -> V): ScatterMap<K, V> {
    val result = mutableScatterMapOf<K, V>()
    this.forEach { element ->
      result.put(element, valueSelector(element))
    }
    return result
  }
}
