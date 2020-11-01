package fobo66.exchangecourcesbelarus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import fobo66.valiutchik.core.entities.BestCurrencyRate
import fobo66.exchangecourcesbelarus.model.LoadExchangeRates
import fobo66.exchangecourcesbelarus.model.RefreshExchangeRates
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

class MainViewModel @Inject constructor(
  private val refreshExchangeRates: RefreshExchangeRates,
  private val loadExchangeRates: LoadExchangeRates
) : ViewModel() {

  val buyOrSell: LiveData<Boolean>
    get() = _buyOrSell

  val bestCurrencyRates: LiveData<List<BestCurrencyRate>>
    get() = buyOrSell
      .switchMap { loadExchangeRates.execute(it).asLiveData() }

  val errors: LiveData<Throwable>
    get() = _errors

  private val _buyOrSell = MutableLiveData(false)
  private val _errors = MutableLiveData<Throwable>()

  private val errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Timber.e(throwable, "Error happened when refreshing currency rates")
    _errors.postValue(throwable)
  }

  fun updateBuySell(buySell: Boolean) {
    _buyOrSell.postValue(buySell)
  }

  fun refreshExchangeRates(latitude: Double, longitude: Double) =
    viewModelScope.launch(errorHandler) {
      refreshExchangeRates.execute(latitude, longitude, LocalDateTime.now())
    }
}