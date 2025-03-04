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
import fobo66.valiutchik.core.util.CurrencyName

/**
 * Datasource for determining best exchange rates in the dataset
 */
interface BestCourseDataSource {
  /**
   * Find best buy rates (what bank buys for) for the given currency
   *
   * @param courses Raw set of rates
   *
   * @return Map of the currency name to its best rate
   */
  fun findBestBuyCurrencies(courses: Set<Bank>): Map<CurrencyName, Bank>

  /**
   * Find best sell rates (what bank sells for) for the given currency
   *
   * @param courses Raw set of rates
   *
   * @return Map of the currency name to its best rate
   */
  fun findBestSellCurrencies(courses: Set<Bank>): Map<CurrencyName, Bank>
}
