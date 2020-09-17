package fobo66.exchangecourcesbelarus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate
import fobo66.exchangecourcesbelarus.entities.toBestCurrencyRate
import fobo66.exchangecourcesbelarus.model.LoadExchangeRates
import fobo66.exchangecourcesbelarus.model.RefreshExchangeRates
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
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
  private val _bestCurrencyRates = MutableLiveData<List<BestCurrencyRate>>()
  private val _errors = MutableLiveData<Throwable>()

  private val errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Timber.e(throwable, "Error happened when refreshing currency rates")
    _errors.postValue(throwable)
  }

  fun updateBuySell(buySell: Boolean) {
    _buyOrSell.postValue(buySell)
  }

  fun loadExchangeRates(latitude: Double, longitude: Double) = viewModelScope.launch(errorHandler) {
    val rates = refreshExchangeRates.execute(latitude, longitude)
      .filter { it.isBuy == buyOrSell.value }
      .map { it.toBestCurrencyRate() }
    _bestCurrencyRates.postValue(rates)
  }
}