/*
 *    Copyright 2024 Andrey Mukamolov
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
import fobo66.exchangecourcesbelarus.entities.MainScreenState
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.CurrencyRatesInteractor
import fobo66.valiutchik.domain.usecases.FindBankOnMap
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
  private val currencyRatesInteractor: CurrencyRatesInteractor,
  private val copyCurrencyRateToClipboard: CopyCurrencyRateToClipboard,
  private val findBankOnMap: FindBankOnMap
) : ViewModel() {

  val bestCurrencyRates = currencyRatesInteractor.loadExchangeRates()
    .filter { it.isNotEmpty() }
    .map {
      it.toImmutableList()
    }
    .onEach {
      state.emit(MainScreenState.LoadedRates)
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
      initialValue = persistentListOf()
    )

  private val state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
  val screenState = state.asStateFlow()

  fun findBankOnMap(bankName: CharSequence): Intent? = findBankOnMap.execute(bankName)

  fun refreshExchangeRates() =
    viewModelScope.launch {
      try {
        state.emit(MainScreenState.Loading)
        currencyRatesInteractor.refreshExchangeRates()
        state.emit(MainScreenState.LoadedRates)
      } catch (e: CurrencyRatesLoadFailedException) {
        Napier.e("Error happened when refreshing currency rates", e)
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
        Napier.e("Error happened when force refreshing currency rates", e)
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
        Napier.e("Error happened when refreshing currency rates in default city", e)
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
        Napier.e("Error happened when refreshing currency rates in default city", e)
        state.emit(MainScreenState.Error)
      }
    }

  fun copyCurrencyRateToClipboard(currencyName: CharSequence, currencyValue: CharSequence) {
    copyCurrencyRateToClipboard.execute(currencyName, currencyValue)
  }
}
