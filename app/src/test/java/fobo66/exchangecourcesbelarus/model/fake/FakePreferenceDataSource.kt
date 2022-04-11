package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource

class FakePreferenceDataSource : PreferencesDataSource, Resettable {
  var string = "default"
  var int = 3

  override fun loadString(key: String, defaultValue: String): String =
    string

  override fun saveString(key: String, value: String) = Unit

  override fun loadInt(key: String, defaultValue: Int): Int = int

  override fun saveInt(key: String, value: Int) = Unit

  override fun reset() {
    string = "default"
    int = 3
  }
}
