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

import dev.fobo66.core.data.testing.fake.FakeGeocodingDataSource
import dev.fobo66.core.data.testing.fake.FakeLocationDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class LocationRepositoryTest {
  private val locationDataSource = FakeLocationDataSource()
  private val geocodingDataSource = FakeGeocodingDataSource()

  private val locationRepository: LocationRepository =
    LocationRepositoryImpl(locationDataSource, geocodingDataSource)

  @Test
  fun `resolve user city`() =
    runTest {
      val city = locationRepository.resolveUserCity("default")
      assertEquals("fake", city)
    }

  @Test
  fun `return default city on HTTP error`() {
    geocodingDataSource.showError = true

    runTest {
      val city = locationRepository.resolveUserCity("default")
      assertEquals("default", city)
    }
  }

  @Test
  fun `crash on unexpected error`() {
    geocodingDataSource.unexpectedError = true

    runTest {
      assertThrows<KotlinNullPointerException> {
        locationRepository.resolveUserCity("default")
      }
    }
  }
}
