package fobo66.exchangecourcesbelarus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fobo66.exchangecourcesbelarus.model.LoadExchangeRates
import javax.inject.Inject

class MainViewModel @Inject constructor(
  private val loadExchangeRates: LoadExchangeRates
) : ViewModel() {

  val buyOrSell: LiveData<Boolean>
    get() = _buyOrSell

  private val _buyOrSell = MutableLiveData(false)

  fun updateBuySell(buySell: Boolean) {
    _buyOrSell.postValue(buySell)
  }
}