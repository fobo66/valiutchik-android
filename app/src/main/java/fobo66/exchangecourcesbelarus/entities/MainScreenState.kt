package fobo66.exchangecourcesbelarus.entities

import androidx.compose.runtime.Stable
import fobo66.valiutchik.core.entities.BestCurrencyRate

sealed class MainScreenState(val isInProgress: Boolean = true) {
  @Stable
  object Loading: MainScreenState()
  @Stable
  data class LoadedRates(val bestCurrencyRates: List<BestCurrencyRate>) : MainScreenState(false)
  @Stable
  object Error : MainScreenState(false)
}
