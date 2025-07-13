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

import androidx.lifecycle.viewModelScope
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreferencesViewModelImpl(
    loadDefaultCityPreference: LoadDefaultCityPreference,
    loadUpdateIntervalPreference: LoadUpdateIntervalPreference,
    private val updateDefaultCityPreference: UpdateDefaultCityPreference,
    private val updateUpdateIntervalPreference: UpdateUpdateIntervalPreference
) : PreferencesViewModel() {

    override val defaultCityPreference: StateFlow<String> = loadDefaultCityPreference.execute()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
            initialValue = ""
        )
    override val updateIntervalPreference: StateFlow<Float> = loadUpdateIntervalPreference.execute()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
            initialValue = 0.0f
        )

    override fun updateDefaultCity(newDefaultCity: String) = viewModelScope.launch {
        updateDefaultCityPreference.execute(newDefaultCity)
    }

    override fun updateUpdateInterval(newUpdateInterval: Float) = viewModelScope.launch {
        updateUpdateIntervalPreference.execute(newUpdateInterval)
    }
}
