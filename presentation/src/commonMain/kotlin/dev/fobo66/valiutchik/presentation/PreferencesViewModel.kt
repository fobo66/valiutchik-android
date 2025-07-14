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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

abstract class PreferencesViewModel : ViewModel() {
    abstract val defaultCityPreference: StateFlow<String>
    abstract val updateIntervalPreference: StateFlow<Float>
    abstract fun updateDefaultCity(newDefaultCity: String): Job
    abstract fun updateUpdateInterval(newUpdateInterval: Float): Job
}
