package fobo66.exchangecourcesbelarus.model.datasource

interface PreferencesDataSource {
  suspend fun loadString(key: String, defaultValue: String = ""): String
  suspend fun saveString(key: String, value: String)
  suspend fun loadLong(key: String, defaultValue: Long = 0L): Long
}
