package fobo66.exchangecourcesbelarus.entities

import androidx.compose.runtime.Immutable

sealed class MainScreenState(val isInProgress: Boolean = true) {
  @Immutable
  object Loading : MainScreenState()

  @Immutable
  object LoadedRates : MainScreenState(false)

  @Immutable
  object Error : MainScreenState(false)
}
