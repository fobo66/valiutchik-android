package fobo66.valiutchik.core.fake

import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePreferenceDataSource : PreferencesDataSource {
  var string = "default"
  var int = 3

  override suspend fun loadString(key: String, defaultValue: String): String =
    string

  override suspend fun saveString(key: String, value: String) = Unit

  override suspend fun loadInt(key: String, defaultValue: Int): Int = int

  override fun observeString(key: String, defaultValue: String): Flow<String> =
    flowOf(string)

  override fun observeInt(key: String, defaultValue: Int): Flow<Int> =
    flowOf(int)

  override suspend fun saveInt(key: String, value: Int) = Unit
}