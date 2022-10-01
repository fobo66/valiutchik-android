package fobo66.exchangecourcesbelarus.entities

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableMap

@Stable
data class ListPreferenceEntries(
  val preferenceEntries: ImmutableMap<String, String>
)
