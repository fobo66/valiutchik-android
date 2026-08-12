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

package dev.fobo66.valiutchik.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.retained.rememberRetained
import dev.fobo66.valiutchik.presentation.entity.MainScreen
import dev.fobo66.valiutchik.presentation.entity.MainScreenEvent
import dev.zacsweers.metro.AppScope
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.domain.usecases.FindBankOnMap
import fobo66.valiutchik.domain.usecases.RatesInteractor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@CircuitInject(MainScreen::class, AppScope::class)
class MainPresenterImpl(
    private val ratesInteractor: RatesInteractor,
    private val copyCurrencyRateToClipboard: CopyCurrencyRateToClipboard,
    private val findBankOnMap: FindBankOnMap
) : MainPresenter {
    @Composable
    override fun present(): MainScreen.State {
        val scope = rememberCoroutineScope()
        var isPermissionGranted by rememberRetained { mutableStateOf(false) }
        val isRefreshing by produceRetainedState(false) {
            ratesInteractor.isRefreshInProgress.collect { value = it }
        }

        val rates: ImmutableList<BestCurrencyRate> by produceRetainedState(
            persistentListOf()
        ) {
            ratesInteractor.rates
                .collect { latest ->
                    value = latest.toPersistentList()
                }
        }

        return MainScreen.State(
            isLoading = isRefreshing,
            rates = rates,
            isLocationPermissionGranted = isPermissionGranted
        ) { event ->
            when (event) {
                is MainScreenEvent.CopyToClipboard -> {
                    scope.launch {
                        copyCurrencyRateToClipboard(event.rate)
                    }
                }

                is MainScreenEvent.OpenOnMap -> scope.launch {
                    findBankOnMap(event.bankName)
                }

                is MainScreenEvent.PermissionStateChanged -> isPermissionGranted = event.isGranted

                MainScreenEvent.Refresh -> scope.launch {
                    ratesInteractor.initiateRefresh(isPermissionGranted)
                }
            }
        }
    }
}
