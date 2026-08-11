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

package fobo66.valiutchik.core.model.repository

import dev.fobo66.valiutchik.core.db.City
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import fobo66.valiutchik.core.KEY_DEFAULT_CITY_ID
import fobo66.valiutchik.core.KEY_UPDATE_INTERVAL
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_UPDATE_INTERVAL = 3f
private const val DEFAULT_CITY_ID = 1L

@ContributesBinding(AppScope::class)
@Inject
class PreferenceRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
    private val persistenceDataSource: PersistenceDataSource
) : PreferenceRepository {

    override fun observeDefaultCityIdPreference(): Flow<Long> =
        preferencesDataSource.observeLong(KEY_DEFAULT_CITY_ID, DEFAULT_CITY_ID)

    override fun observeCities(): Flow<List<City>> = persistenceDataSource.readCities()

    override fun observeUpdateIntervalPreference(): Flow<Float> =
        preferencesDataSource.observeFloat(KEY_UPDATE_INTERVAL, DEFAULT_UPDATE_INTERVAL)

    override suspend fun updateDefaultCityIdPreference(newValue: Long) {
        preferencesDataSource.saveLong(KEY_DEFAULT_CITY_ID, newValue)
    }

    override suspend fun updateUpdateIntervalPreference(newValue: Float) {
        preferencesDataSource.saveFloat(KEY_UPDATE_INTERVAL, newValue)
    }
}
