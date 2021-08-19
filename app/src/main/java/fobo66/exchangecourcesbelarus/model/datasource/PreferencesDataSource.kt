package fobo66.exchangecourcesbelarus.model.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.reflect.KClass

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

  @ExperimentalCoroutinesApi
  fun <T : Any> onPreferenceChanges(resultType: KClass<T>): Flow<Preference<T>> =
    callbackFlow {
      val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        val prefsValue: Any = when (resultType) {
          String::class -> prefs.getString(key, "").orEmpty()
          Int::class -> prefs.getInt(key, 0)
          Long::class -> prefs.getLong(key, 0L)
          Boolean::class -> prefs.getBoolean(key, false)
          else -> cancel(
            CancellationException(
              "Failed to get preference changes",
              IllegalStateException("Not supported type " + resultType.simpleName)
            )
          )
        }
        trySend(Preference(key, prefsValue as T))
      }

      preferences.registerOnSharedPreferenceChangeListener(listener)

      awaitClose {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
      }
    }
}

data class Preference<T>(
  val key: String,
  val value: T
)
