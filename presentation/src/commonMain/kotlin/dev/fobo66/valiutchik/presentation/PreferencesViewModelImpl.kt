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

import androidx.lifecycle.viewModelScope
import fobo66.valiutchik.domain.entities.CityPreference
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreferenceValues
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PreferencesViewModelImpl(
    loadDefaultCityPreference: LoadDefaultCityPreference,
    loadUpdateIntervalPreference: LoadUpdateIntervalPreference,
    loadDefaultCityPreferenceValues: LoadDefaultCityPreferenceValues,
    private val updateDefaultCityPreference: UpdateDefaultCityPreference,
    private val updateUpdateIntervalPreference: UpdateUpdateIntervalPreference
) : PreferencesViewModel() {

    override val defaultCityPreference: StateFlow<Long> = loadDefaultCityPreference.execute()
        .stateInWhileSubscribed(initialValue = 0L)
    override val defaultCityPreferenceValues: StateFlow<ImmutableList<CityPreference>> =
        loadDefaultCityPreferenceValues.execute()
            .map { it.toImmutableList() }
            .stateInWhileSubscribed(initialValue = persistentListOf())
    override val updateIntervalPreference: StateFlow<Float> = loadUpdateIntervalPreference.execute()
        .stateInWhileSubscribed(initialValue = 0.0f)

    override fun updateDefaultCity(newDefaultCity: String) = viewModelScope.launch {
        updateDefaultCityPreference.execute(newDefaultCity.toLong())
    }

    override fun updateUpdateInterval(newUpdateInterval: Float) = viewModelScope.launch {
        updateUpdateIntervalPreference.execute(newUpdateInterval)
    }
}
