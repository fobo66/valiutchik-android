package fobo66.exchangecourcesbelarus.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.util.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.entities.BestCurrencyRate
import fobo66.valiutchik.core.usecases.CopyCurrencyRateToClipboard
import fobo66.valiutchik.core.usecases.FindBankOnMap
import fobo66.valiutchik.core.usecases.LoadExchangeRates
import fobo66.valiutchik.core.usecases.RefreshExchangeRates
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MainViewModel @Inject constructor(
  private val refreshExchangeRates: RefreshExchangeRates,
  private val loadExchangeRates: LoadExchangeRates,
  private val copyCurrencyRateToClipboard: CopyCurrencyRateToClipboard,
  private val findBankOnMap: FindBankOnMap
) : ViewModel() {

  val bestCurrencyRates: Flow<List<BestCurrencyRate>>
    get() = loadExchangeRates.execute()

  val errors: SharedFlow<Unit>
    get() = _errors

  private val _errors = MutableSharedFlow<Unit>()
  private val _progress = MutableStateFlow(false)
  val progress = _progress.asStateFlow()

  fun findBankOnMap(bankName: CharSequence): Intent? {
    return findBankOnMap.execute(bankName)
  }

  fun refreshExchangeRates() =
    viewModelScope.launch {
      try {
        showProgress()
        refreshExchangeRates.execute(LocalDateTime.now())
      } catch (e: CurrencyRatesLoadFailedException) {
        Timber.e(e, "Error happened when refreshing currency rates")
        _errors.emit(Unit)
      } finally {
        hideProgress()
      }
    }

  fun copyCurrencyRateToClipboard(currencyName: CharSequence, currencyValue: CharSequence) {
    copyCurrencyRateToClipboard.execute(currencyName, currencyValue)
  }

  private suspend fun showProgress() {
    _progress.emit(true)
  }

  suspend fun hideProgress() {
    _progress.emit(false)
  }
}
