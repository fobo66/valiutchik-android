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

package dev.fobo66.core.data.testing.fake

import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.Rate
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakePersistenceDataSource : PersistenceDataSource {
    var isSaved = false

    override suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
        isSaved = true
    }

    override suspend fun saveRates(rates: List<Rate>) {
        isSaved = true
    }

    override fun readBestCourses(): Flow<List<BestCourse>> = emptyFlow()
}
