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

package fobo66.valiutchik.core.model.repository

import dev.fobo66.core.data.testing.fake.FakeApiDataSource
import dev.fobo66.core.data.testing.fake.FakePersistenceDataSource
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class DataRefreshRepositoryTest {
    private val persistenceDataSource = FakePersistenceDataSource()
    private val apiDataSource = FakeApiDataSource()

    private val dataRefreshRepository: DataRefreshRepository = DataRefreshRepositoryImpl(
        apiDataSource,
        persistenceDataSource,
        UnconfinedTestDispatcher()
    )

    @Test
    fun `sync data`() = runTest {
        dataRefreshRepository.refresh()
        assertTrue { persistenceDataSource.isSaved }
    }

    @Test
    fun `sync data fails`() = runTest {
        apiDataSource.isError = true
        assertFails {
            dataRefreshRepository.refresh()
        }
    }
}
