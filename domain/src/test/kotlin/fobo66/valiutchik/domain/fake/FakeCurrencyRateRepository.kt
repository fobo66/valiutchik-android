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

package fobo66.valiutchik.domain.fake

import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCurrencyRateRepository : CurrencyRateRepository {
  var isRefreshed = false

  override suspend fun refreshExchangeRates(city: String, now: LocalDateTime) {
    isRefreshed = true
  }

  override fun loadExchangeRates(): Flow<List<BestCourse>> = flowOf(emptyList())
}
