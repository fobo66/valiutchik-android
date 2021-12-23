package fobo66.exchangecourcesbelarus.model.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import fobo66.exchangecourcesbelarus.di.Io
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(
  private val preferences: SharedPreferences,
  @Io private val ioDispatcher: CoroutineDispatcher
) : PreferencesDataSource {
  override suspend fun loadString(key: String, defaultValue: String): String =
    withContext(ioDispatcher) {
      preferences.getString(key, defaultValue).orEmpty()
    }

  override suspend fun saveString(key: String, value: String) = withContext(ioDispatcher) {
    preferences.edit {
      putString(key, value)
    }
  }

  override fun loadInt(key: String, defaultValue: Int): Int =
    preferences.getInt(key, defaultValue)
}
