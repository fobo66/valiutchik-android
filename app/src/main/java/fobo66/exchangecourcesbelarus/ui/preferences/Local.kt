package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy

val LocalPreferenceEnabledStatus: ProvidableCompositionLocal<Boolean> =
  compositionLocalOf(structuralEqualityPolicy()) { true }
