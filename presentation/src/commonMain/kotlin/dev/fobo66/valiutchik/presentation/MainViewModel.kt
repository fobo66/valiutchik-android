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

import androidx.lifecycle.ViewModel
import dev.fobo66.valiutchik.presentation.entity.MainScreenState
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

abstract class MainViewModel : ViewModel() {
    abstract val bestCurrencyRates: StateFlow<ImmutableList<BestCurrencyRate>>
    abstract val screenState: StateFlow<MainScreenState>

    abstract fun findBankOnMap(bankName: CharSequence): Boolean
    abstract fun manualRefresh(): Job
    abstract fun handleLocationPermission(permissionGranted: Boolean): Job
    abstract fun copyCurrencyRateToClipboard(currencyValue: CharSequence)
}
