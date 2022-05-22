package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource

class FakePreferenceDataSource : PreferencesDataSource, Resettable {
  var string = "default"
  var int = 3

  override suspend fun loadString(key: String, defaultValue: String): String =
    string

  override suspend fun saveString(key: String, value: String) = Unit

  override suspend fun loadInt(key: String, defaultValue: Int): Int = int

  override fun reset() {
    string = "default"
    int = 3
  }
}
