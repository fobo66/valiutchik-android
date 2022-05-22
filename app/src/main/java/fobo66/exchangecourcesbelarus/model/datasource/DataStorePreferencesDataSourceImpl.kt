package fobo66.exchangecourcesbelarus.model.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class DataStorePreferencesDataSourceImpl @Inject constructor(
  private val dataStore: DataStore<Preferences>
) : PreferencesDataSource {
  override suspend fun loadString(key: String, defaultValue: String): String {
    return dataStore.data.map { it[stringPreferencesKey(key)] }.last() ?: defaultValue
  }

  override suspend fun saveString(key: String, value: String) {
    dataStore.edit {
      it[stringPreferencesKey(key)] = value
    }
  }

  override suspend fun loadInt(key: String, defaultValue: Int): Int {
    return dataStore.data.map { it[intPreferencesKey(key)] }.last() ?: defaultValue
  }
}
