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

package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.KEY_DEFAULT_CITY
import fobo66.valiutchik.core.KEY_UPDATE_INTERVAL
import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DEFAULT_UPDATE_INTERVAL = 3
private const val DEFAULT_CITY = "Minsk"

class PreferenceRepositoryImpl(private val preferencesDataSource: PreferencesDataSource) :
    PreferenceRepository {
    override fun observeDefaultCityPreference(): Flow<String> =
        preferencesDataSource.observeString(KEY_DEFAULT_CITY, DEFAULT_CITY)

    override fun observeUpdateIntervalPreference(): Flow<Float> =
        preferencesDataSource.observeInt(KEY_UPDATE_INTERVAL, DEFAULT_UPDATE_INTERVAL)
            .map { it.toFloat() }

    override suspend fun updateDefaultCityPreference(newValue: String) {
        preferencesDataSource.saveString(KEY_DEFAULT_CITY, newValue)
    }

    override suspend fun updateUpdateIntervalPreference(newValue: Float) {
        preferencesDataSource.saveInt(KEY_UPDATE_INTERVAL, newValue.roundToInt())
    }
}
