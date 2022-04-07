package fobo66.exchangecourcesbelarus.model.datasource

interface PreferencesDataSource {
  fun loadString(key: String, defaultValue: String = ""): String
  fun saveString(key: String, value: String)
  fun loadInt(key: String, defaultValue: Int = 0): Int
  fun saveInt(key: String, value: Int)
}
