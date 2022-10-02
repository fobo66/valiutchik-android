package fobo66.exchangecourcesbelarus.model.datasource

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
  suspend fun loadString(key: String, defaultValue: String = ""): String
  suspend fun saveString(key: String, value: String)
  suspend fun loadInt(key: String, defaultValue: Int = 0): Int
  fun observeString(key: String, defaultValue: String): Flow<String>
  fun observeInt(key: String, defaultValue: Int): Flow<Int>
  suspend fun saveInt(key: String, value: Int)
}
