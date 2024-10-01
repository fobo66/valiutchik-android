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
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import fobo66.exchangecourcesbelarus.entities.MainScreenState
import fobo66.exchangecourcesbelarus.work.RatesRefreshWorker
import fobo66.exchangecourcesbelarus.work.WORKER_ARG_LOCATION_AVAILABLE
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.CurrencyRatesInteractor
import fobo66.valiutchik.domain.usecases.FindBankOnMap
import java.util.concurrent.TimeUnit
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val WORK_BACKGROUND_REFRESH = "backgroundRefresh"

class MainViewModel(
  private val currencyRatesInteractor: CurrencyRatesInteractor,
  private val copyCurrencyRateToClipboard: CopyCurrencyRateToClipboard,
  private val findBankOnMap: FindBankOnMap,
  private val workManager: WorkManager
) : ViewModel() {

  val bestCurrencyRates = currencyRatesInteractor.loadExchangeRates()
    .filter { it.isNotEmpty() }
    .map { it.toImmutableList() }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
      initialValue = persistentListOf()
    )

  private val isLocationPermissionGranted = MutableStateFlow(false)
  private val isRefreshTriggered = MutableStateFlow(true)

  @OptIn(ExperimentalCoroutinesApi::class)
  val screenState = combine(
    isLocationPermissionGranted,
    isRefreshTriggered
  ) { locationGranted, refreshTriggered -> locationGranted to refreshTriggered }
    .filter { (_, refreshTriggered) -> refreshTriggered }
    .onEach { (locationGranted, _) -> handleRefresh(locationGranted) }
    .flatMapLatest { workManager.getWorkInfosForUniqueWorkFlow(WORK_BACKGROUND_REFRESH) }
    .map { infos ->
      infos.any { info ->
        info.state == WorkInfo.State.RUNNING
      }
    }
    .map {
      if (it) {
        MainScreenState.Loading
      } else {
        MainScreenState.LoadedRates
      }
    }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
      initialValue = MainScreenState.Initial
    )

  fun findBankOnMap(bankName: CharSequence): Intent? = findBankOnMap.execute(bankName)

  fun handleRefresh(isLocationAvailable: Boolean) = viewModelScope.launch {
    val updateInterval = currencyRatesInteractor.loadUpdateInterval().first()
    val workRequest = PeriodicWorkRequestBuilder<RatesRefreshWorker>(updateInterval, TimeUnit.HOURS)
      .setConstraints(
        Constraints.Builder()
          .setRequiresBatteryNotLow(true)
          .setRequiredNetworkType(NetworkType.NOT_ROAMING)
          .build()
      )
      .setInputData(workDataOf(WORKER_ARG_LOCATION_AVAILABLE to isLocationAvailable))
      .build()
    workManager.enqueueUniquePeriodicWork(
      WORK_BACKGROUND_REFRESH,
      ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
      workRequest
    )
  }

  fun handleLocationPermission(permissionGranted: Boolean) = viewModelScope.launch {
    isLocationPermissionGranted.emit(permissionGranted)
  }

  fun copyCurrencyRateToClipboard(currencyName: CharSequence, currencyValue: CharSequence) {
    copyCurrencyRateToClipboard.execute(currencyName, currencyValue)
  }
}
