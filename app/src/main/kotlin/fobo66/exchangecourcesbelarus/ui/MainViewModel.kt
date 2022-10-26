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

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.entities.MainScreenState
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.CurrencyRatesInteractor
import fobo66.valiutchik.domain.usecases.FindBankOnMap
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MainViewModel @Inject constructor(
  private val currencyRatesInteractor: CurrencyRatesInteractor,
  private val copyCurrencyRateToClipboard: CopyCurrencyRateToClipboard,
  private val findBankOnMap: FindBankOnMap
) : ViewModel() {

  val bestCurrencyRates = currencyRatesInteractor.loadExchangeRates()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
      initialValue = emptyList()
    )
    .filter { it.isNotEmpty() }
    .map {
      it.toImmutableList()
    }
    .onEach {
      state.emit(MainScreenState.LoadedRates)
    }

  private val state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
  val screenState = state.asStateFlow()

  fun findBankOnMap(bankName: CharSequence): Intent? {
    return findBankOnMap.execute(bankName)
  }

  fun refreshExchangeRates() =
    viewModelScope.launch {
      try {
        state.emit(MainScreenState.Loading)
        currencyRatesInteractor.refreshExchangeRates()
        state.emit(MainScreenState.LoadedRates)
      } catch (e: CurrencyRatesLoadFailedException) {
        Timber.e(e, "Error happened when refreshing currency rates")
        state.emit(MainScreenState.Error)
      }
    }

  fun forceRefreshExchangeRates() =
    viewModelScope.launch {
      try {
        state.emit(MainScreenState.Loading)
        currencyRatesInteractor.forceRefreshExchangeRates()
        state.emit(MainScreenState.LoadedRates)
      } catch (e: CurrencyRatesLoadFailedException) {
        Timber.e(e, "Error happened when force refreshing currency rates")
        state.emit(MainScreenState.Error)
      }
    }

  fun refreshExchangeRatesForDefaultCity() =
    viewModelScope.launch {
      try {
        state.emit(MainScreenState.Loading)
        currencyRatesInteractor.refreshExchangeRatesForDefaultCity()
        state.emit(MainScreenState.LoadedRates)
      } catch (e: CurrencyRatesLoadFailedException) {
        Timber.e(e, "Error happened when refreshing currency rates in default city")
        state.emit(MainScreenState.Error)
      }
    }

  fun forceRefreshExchangeRatesForDefaultCity() =
    viewModelScope.launch {
      try {
        state.emit(MainScreenState.Loading)
        currencyRatesInteractor.forceRefreshExchangeRatesForDefaultCity()
        state.emit(MainScreenState.LoadedRates)
      } catch (e: CurrencyRatesLoadFailedException) {
        Timber.e(e, "Error happened when refreshing currency rates in default city")
        state.emit(MainScreenState.Error)
      }
    }

  fun copyCurrencyRateToClipboard(currencyName: CharSequence, currencyValue: CharSequence) {
    copyCurrencyRateToClipboard.execute(currencyName, currencyValue)
  }
}
