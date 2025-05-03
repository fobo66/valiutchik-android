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

package fobo66.exchangecourcesbelarus.ui.preferences

import app.cash.turbine.test
import dev.fobo66.domain.testing.fake.FakeDefaultCityPreference
import dev.fobo66.domain.testing.fake.FakeUpdateIntervalPreference
import fobo66.valiutchik.core.KEY_DEFAULT_CITY
import fobo66.valiutchik.core.KEY_UPDATE_INTERVAL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val INTERVAL = 1f
private const val NEW_INTERVAL = 3f

private const val CITY = "test"
private const val NEW_CITY = "newcity"

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesViewModelTest {
  private lateinit var viewModel: PreferencesViewModel

  private val fakeStorage = buildMap {
    put(KEY_DEFAULT_CITY, CITY)
    put(KEY_UPDATE_INTERVAL, INTERVAL.toString())
  }.toMutableMap()

  private val defaultCityPreference = FakeDefaultCityPreference(fakeStorage)
  private val updateIntervalPreference = FakeUpdateIntervalPreference(fakeStorage)

  @BeforeEach
  fun setUp() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    viewModel = PreferencesViewModel(
      defaultCityPreference,
      updateIntervalPreference,
      defaultCityPreference,
      updateIntervalPreference
    )
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `load default city pref`() = runTest {
    viewModel.defaultCityPreference.test {
      val pref = awaitItem()
      assertEquals(CITY, pref)
    }
  }

  @Test
  fun `update default city pref`() = runTest {
    viewModel.updateDefaultCity(NEW_CITY)

    viewModel.defaultCityPreference.test {
      val pref = awaitItem()
      assertEquals(NEW_CITY, pref)
    }
  }

  @Test
  fun `load update interval pref`() = runTest {
    viewModel.updateIntervalPreference.test {
      val pref = awaitItem()
      assertEquals(INTERVAL, pref)
    }
  }

  @Test
  fun `update update interval pref`() = runTest {
    viewModel.updateUpdateInterval(NEW_INTERVAL)

    viewModel.updateIntervalPreference.test {
      val pref = awaitItem()
      assertEquals(NEW_INTERVAL, pref)
    }
  }
}
