package fobo66.exchangecourcesbelarus.model.datasource

interface PreferencesDataSource {
  suspend fun loadString(key: String, defaultValue: String = ""): String
  fun saveString(key: String, value: String)
  fun loadInt(key: String, defaultValue: Int = 0): Int
}
