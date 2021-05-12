package fobo66.exchangecourcesbelarus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.model.LoadExchangeRates
import fobo66.exchangecourcesbelarus.model.RefreshExchangeRates
import fobo66.valiutchik.core.entities.BestCurrencyRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val refreshExchangeRates: RefreshExchangeRates,
  private val loadExchangeRates: LoadExchangeRates
) : ViewModel() {

  val buyOrSell: StateFlow<Boolean>
    get() = _buyOrSell

  @ExperimentalCoroutinesApi
  val bestCurrencyRates: Flow<List<BestCurrencyRate>>
    get() = buyOrSell
      .flatMapLatest { loadExchangeRates.execute(it) }

  val errors: SharedFlow<Unit>
    get() = _errors

  private val _buyOrSell = MutableStateFlow(false)
  private val _errors = MutableSharedFlow<Unit>()

  suspend fun updateBuySell(buySell: Boolean) {
    _buyOrSell.emit(buySell)
  }

  @ExperimentalCoroutinesApi
  fun refreshExchangeRates(latitude: Double, longitude: Double) =
    viewModelScope.launch {
      try {
        refreshExchangeRates.execute(latitude, longitude, LocalDateTime.now())
      } catch (e: Throwable) {
        Timber.e(e, "Error happened when refreshing currency rates")
        _errors.emit(Unit)
      }
    }
}
