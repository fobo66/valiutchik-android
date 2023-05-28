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

import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import fobo66.valiutchik.core.entities.BestCourse
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/19/19.
 */
@Single
class PersistenceDataSourceImpl(
  private val database: CurrencyRatesDatabase
) : PersistenceDataSource {

  override suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
    database.currencyRatesDao().insertBestCurrencyRates(bestCourses)
  }

  override fun readBestCourses(): Flow<List<BestCourse>> =
    database.currencyRatesDao().loadLatestBestCurrencyRates()
}
