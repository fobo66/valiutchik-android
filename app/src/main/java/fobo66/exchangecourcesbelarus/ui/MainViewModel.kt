package fobo66.exchangecourcesbelarus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate
import fobo66.exchangecourcesbelarus.entities.toBestCurrencyRate
import fobo66.exchangecourcesbelarus.model.LoadExchangeRates
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
  private val loadExchangeRates: LoadExchangeRates
) : ViewModel() {

  val buyOrSell: LiveData<Boolean>
    get() = _buyOrSell
  val bestCurrencyRates: LiveData<List<BestCurrencyRate>>
    get() = _bestCurrencyRates

  private val _buyOrSell = MutableLiveData(false)
  private val _bestCurrencyRates = MutableLiveData<List<BestCurrencyRate>>()

  fun updateBuySell(buySell: Boolean) {
    _buyOrSell.postValue(buySell)
  }

  fun loadExchangeRates(latitude: Double, longitude: Double) = viewModelScope.launch {
    val rates = loadExchangeRates.execute(latitude, longitude)
      .filter { it.isBuy == buyOrSell.value }
      .map { it.toBestCurrencyRate() }
    _bestCurrencyRates.postValue(rates)
  }
}
