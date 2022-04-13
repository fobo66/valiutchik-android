package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource

class FakePreferenceDataSource : PreferencesDataSource, Resettable {
  var string = "default"
  var long = 3L

  override suspend fun loadString(key: String, defaultValue: String): String =
    string

  override suspend fun saveString(key: String, value: String) = Unit

  override suspend fun loadLong(key: String, defaultValue: Long): Long = long

  override fun reset() {
    string = "default"
    long = 3L
  }
}
