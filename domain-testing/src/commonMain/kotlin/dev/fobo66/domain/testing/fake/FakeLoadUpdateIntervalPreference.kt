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

package dev.fobo66.domain.testing.fake

import fobo66.valiutchik.core.KEY_UPDATE_INTERVAL
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeLoadUpdateIntervalPreference(private val storage: Map<String, String>) :
    LoadUpdateIntervalPreference {
    override fun execute(): Flow<Float> = flowOf(storage)
        .map { it.getValue(KEY_UPDATE_INTERVAL).toFloat() }
}
