package fobo66.exchangecourcesbelarus.model.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStorePreferencesDataSourceImpl @Inject constructor(
  private val dataStore: DataStore<Preferences>
) : PreferencesDataSource {
  override suspend fun loadString(key: String, defaultValue: String): String {
    return dataStore.data.map { it[stringPreferencesKey(key)] }.last() ?: defaultValue
  }

  override fun saveString(key: String, value: String) {
    TODO("Not yet implemented")
  }

  override fun loadInt(key: String, defaultValue: Int): Int {
    TODO("Not yet implemented")
  }
}
