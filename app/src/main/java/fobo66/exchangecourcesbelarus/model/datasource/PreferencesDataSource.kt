package fobo66.exchangecourcesbelarus.model.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
  private val preferences: SharedPreferences
) {
  fun loadString(key: String, defaultValue: String = ""): String =
    preferences.getString(key, defaultValue).orEmpty()

  fun saveString(key: String, value: String) = preferences.edit {
    putString(key, value)
  }

  fun loadInt(key: String, defaultValue: Int = 0): Int =
    preferences.getInt(key, defaultValue)

  fun saveInt(key: String, value: Int) = preferences.edit {
    putInt(key, value)
  }
}
