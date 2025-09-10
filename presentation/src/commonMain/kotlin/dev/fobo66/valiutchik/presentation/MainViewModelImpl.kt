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

import dev.fobo66.valiutchik.presentation.entity.MainScreenState
import dev.fobo66.valiutchik.presentation.entity.MainScreenStateTrigger
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.FindBankOnMap
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import fobo66.valiutchik.domain.usecases.RefreshInteractor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MainViewModelImpl(
    loadExchangeRates: LoadExchangeRates,
    private val copyCurrencyRateToClipboard: CopyCurrencyRateToClipboard,
    private val findBankOnMap: FindBankOnMap,
    private val refreshInteractor: RefreshInteractor
) : MainViewModel() {
    override val bestCurrencyRates: StateFlow<ImmutableList<BestCurrencyRate>> =
        loadExchangeRates.execute()
            .onEach {
                if (it.isEmpty()) {
                    isRefreshTriggered.emit(true)
                }
            }.map { it.toImmutableList() }
            .stateInWhileSubscribed(
                initialValue = persistentListOf()
            )

    private val isLocationPermissionGranted: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private val isRefreshTriggered = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val screenState: StateFlow<MainScreenState> =
        combine(
            isRefreshTriggered,
            isLocationPermissionGranted,
            ::MainScreenStateTrigger
        ).filter { it.isRefreshTriggered && it.isLocationAvailable != null }
            .onEach {
                refreshInteractor.initiateRefresh(it.isLocationAvailable == true)
                isRefreshTriggered.emit(false)
            }.flatMapLatest { refreshInteractor.isRefreshInProgress }
            .map {
                if (it) {
                    MainScreenState.Loading
                } else {
                    MainScreenState.LoadedRates
                }
            }.stateInWhileSubscribed(
                initialValue = MainScreenState.Initial
            )

    override fun findBankOnMap(bankName: CharSequence): Boolean = findBankOnMap.execute(bankName)

    override fun manualRefresh() = isRefreshTriggered.update {
        true
    }

    override fun handleLocationPermission(permissionGranted: Boolean) =
        isLocationPermissionGranted.update {
            if (it != permissionGranted) {
                isRefreshTriggered.update { true }
            }
            permissionGranted
        }

    override fun copyCurrencyRateToClipboard(currencyValue: CharSequence) {
        copyCurrencyRateToClipboard.execute(currencyValue)
    }
}
