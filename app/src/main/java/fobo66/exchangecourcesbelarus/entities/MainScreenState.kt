package fobo66.exchangecourcesbelarus.entities

import androidx.compose.runtime.Stable

sealed class MainScreenState(val isInProgress: Boolean = true) {
  @Stable
  object Loading : MainScreenState()

  @Stable
  object LoadedRates : MainScreenState(false)

  @Stable
  object Error : MainScreenState(false)
}
