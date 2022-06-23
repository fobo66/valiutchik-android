package fobo66.exchangecourcesbelarus.entities

import androidx.compose.runtime.Stable

sealed class PreferenceScreenState {
  object Loading : PreferenceScreenState()
  data class LoadedPreferences(@Stable val preferences: List<Preference>) : PreferenceScreenState()
}
