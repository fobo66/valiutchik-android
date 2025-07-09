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

package fobo66.valiutchik.core.model.repository

import androidx.test.filters.SmallTest
import dev.fobo66.core.data.testing.fake.FakeIntentDataSource
import dev.fobo66.core.data.testing.fake.FakeUriDataSource
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

@SmallTest
class MapRepositoryImplTest {
    private val uriDataSource = FakeUriDataSource()
    private val intentDataSource =
        FakeIntentDataSource()

    private val mapRepository: MapRepository = MapRepositoryImpl(uriDataSource, intentDataSource)

    @Test
    fun canResolveIntent() {
        assertNotNull(mapRepository.searchOnMap("test"))
    }

    @Test
    fun cannotResolveIntent() {
        intentDataSource.canResolveIntent = false

        assertNull(mapRepository.searchOnMap("test"))
    }
}
