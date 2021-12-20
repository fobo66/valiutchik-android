package fobo66.exchangecourcesbelarus.model.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(
  private val preferences: SharedPreferences
) : PreferencesDataSource {
  override fun loadString(key: String, defaultValue: String): String =
    preferences.getString(key, defaultValue).orEmpty()

  override fun saveString(key: String, value: String) = preferences.edit {
    putString(key, value)
  }

  override fun loadInt(key: String, defaultValue: Int): Int =
    preferences.getInt(key, defaultValue)

  override fun saveInt(key: String, value: Int) = preferences.edit {
    putInt(key, value)
  }
}
