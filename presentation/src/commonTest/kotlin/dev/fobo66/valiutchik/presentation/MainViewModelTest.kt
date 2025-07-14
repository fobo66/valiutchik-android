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

package dev.fobo66.valiutchik.presentation

import app.cash.turbine.test
import dev.fobo66.domain.testing.fake.FakeCopyCurrencyRateToClipboard
import dev.fobo66.domain.testing.fake.FakeFindBankOnMap
import dev.fobo66.domain.testing.fake.FakeLoadExchangeRates
import dev.fobo66.domain.testing.fake.FakeRefreshInteractor
import dev.fobo66.valiutchik.presentation.entity.MainScreenState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    private val loadExchangeRates = FakeLoadExchangeRates()
    private val copyCurrencyRateToClipboard = FakeCopyCurrencyRateToClipboard()
    private val findBankOnMap = FakeFindBankOnMap()
    private val refreshInteractor = FakeRefreshInteractor()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MainViewModelImpl(
            loadExchangeRates,
            copyCurrencyRateToClipboard,
            findBankOnMap,
            refreshInteractor
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `default state`() = runTest {
        viewModel.screenState.test {
            assertEquals(MainScreenState.Initial, awaitItem())
        }
    }

    @Test
    fun `loaded rates`() = runTest {
        viewModel.handleLocationPermission(permissionGranted = false)
        viewModel.screenState.test {
            assertEquals(MainScreenState.LoadedRates, awaitItem())
        }
    }

    @Test
    fun `loading progress`() = runTest {
        refreshInteractor.isInProgress.emit(true)
        viewModel.handleLocationPermission(permissionGranted = false)
        viewModel.screenState.test {
            assertEquals(MainScreenState.Loading, awaitItem())
        }
    }

    @Test
    fun `trigger refresh without location`() = runTest {
        viewModel.manualRefresh()
        viewModel.screenState.test {
            assertEquals(MainScreenState.Initial, awaitItem())
        }
    }
}
