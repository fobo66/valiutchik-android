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

package fobo66.valiutchik.core.model.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePreferencesDataSourceImpl(private val dataStore: DataStore<Preferences>) :
    PreferencesDataSource {

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> = dataStore.data.map {
        it[longPreferencesKey(key)] ?: defaultValue
    }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> = dataStore.data.map {
        it[floatPreferencesKey(key)] ?: defaultValue
    }

    override suspend fun saveLong(key: String, value: Long) {
        dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    override suspend fun saveFloat(key: String, value: Float) {
        dataStore.edit {
            it[floatPreferencesKey(key)] = value
        }
    }
}
