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

package fobo66.exchangecourcesbelarus.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import fobo66.exchangecourcesbelarus.entities.MainScreenState.LoadedRates
import fobo66.exchangecourcesbelarus.entities.MainScreenState.Loading
import fobo66.exchangecourcesbelarus.fake.FakeCopyCurrencyRateToClipboard
import fobo66.exchangecourcesbelarus.fake.FakeCurrencyRatesInteractor
import fobo66.exchangecourcesbelarus.fake.FakeFindBankOnMap
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
  private val currencyRatesInteractor = FakeCurrencyRatesInteractor()
  private val copyCurrencyRateToClipboard = FakeCopyCurrencyRateToClipboard()
  private val findBankOnMap = FakeFindBankOnMap()

  private lateinit var viewModel: MainViewModel

  @BeforeEach
  fun setUp() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    viewModel = MainViewModel(
      currencyRatesInteractor,
      copyCurrencyRateToClipboard,
      findBankOnMap
    )
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial view state`() = runTest {
    viewModel.screenState.test {
      val state = awaitItem()
      assertEquals(Loading, state)
    }
  }

  @Test
  fun `change view state after refresh`() = runTest {
    viewModel.refreshExchangeRates()
    viewModel.screenState.test {
      val state = awaitItem()
      assertEquals(LoadedRates, state)
    }
  }

  @Test
  fun `change view state after force refresh`() = runTest {
    viewModel.forceRefreshExchangeRates()
    viewModel.screenState.test {
      val state = awaitItem()
      assertEquals(LoadedRates, state)
    }
  }

  @Test
  fun `change view state after default refresh`() = runTest {
    viewModel.refreshExchangeRatesForDefaultCity()
    viewModel.screenState.test {
      val state = awaitItem()
      assertEquals(LoadedRates, state)
    }
  }

  @Test
  fun `change view state after default force refresh`() = runTest {
    viewModel.forceRefreshExchangeRatesForDefaultCity()
    viewModel.screenState.test {
      val state = awaitItem()
      assertEquals(LoadedRates, state)
    }
  }

  @Test
  fun `change view state after loading rates`() = runTest {
    val rates = listOf(BestCurrencyRate(0, "test", 0, "test"))
    currencyRatesInteractor.bestCourses.emit(rates)
    viewModel.bestCurrencyRates.test {
      val actualRates = awaitItem()
      assertThat(actualRates).containsExactlyElementsIn(rates)
    }
    viewModel.screenState.test {
      val state = awaitItem()
      assertEquals(LoadedRates, state)
    }
  }

  @Test
  fun `copy to clipboard`() = runTest {
    viewModel.copyCurrencyRateToClipboard("test", "test")
    assertTrue(copyCurrencyRateToClipboard.isCopied)
  }

  @Test
  fun `find bank on map`() = runTest {
    viewModel.findBankOnMap("test")
    assertTrue(findBankOnMap.isFindBankOnMapRequested)
  }

  @Test
  fun `refresh exchange rates`() = runTest {
    viewModel.refreshExchangeRates()
    assertTrue(currencyRatesInteractor.isRefreshed)
    assertFalse(currencyRatesInteractor.isForceRefreshed)
  }

  @Test
  fun `force refresh exchange rates`() = runTest {
    viewModel.forceRefreshExchangeRates()
    assertTrue(currencyRatesInteractor.isForceRefreshed)
    assertFalse(currencyRatesInteractor.isRefreshed)
  }

  @Test
  fun `refresh exchange rates for default city`() = runTest {
    viewModel.refreshExchangeRatesForDefaultCity()
    assertTrue(currencyRatesInteractor.isDefaultRefreshed)
    assertFalse(currencyRatesInteractor.isRefreshed)
    assertFalse(currencyRatesInteractor.isForceRefreshed)
    assertFalse(currencyRatesInteractor.isDefaultForceRefreshed)
  }

  @Test
  fun `force refresh exchange rates for default city`() = runTest {
    viewModel.forceRefreshExchangeRatesForDefaultCity()
    assertTrue(currencyRatesInteractor.isDefaultForceRefreshed)
    assertFalse(currencyRatesInteractor.isDefaultRefreshed)
    assertFalse(currencyRatesInteractor.isRefreshed)
    assertFalse(currencyRatesInteractor.isForceRefreshed)
  }
}
