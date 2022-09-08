package fobo66.exchangecourcesbelarus.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.entities.MainScreenState
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
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
      .filter { it.isNotEmpty() }
      .onEach {
        _mainScreenState.emit(MainScreenState.LoadedRates)
      }

  val errors: SharedFlow<Unit>
    get() = _errors

  private val _errors = MutableSharedFlow<Unit>()
  private val _progress = MutableStateFlow(false)
  private val _mainScreenState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
  val progress = _progress.asStateFlow()
  val mainScreenState = _mainScreenState.asStateFlow()

  fun findBankOnMap(bankName: CharSequence): Intent? {
    return findBankOnMap.execute(bankName)
  }

  fun refreshExchangeRates() =
    viewModelScope.launch {
      try {
        showProgress()
        refreshExchangeRates.execute(LocalDateTime.now())
        _mainScreenState.emit(MainScreenState.LoadedRates)
      } catch (e: CurrencyRatesLoadFailedException) {
        Timber.e(e, "Error happened when refreshing currency rates")
        _errors.emit(Unit)
        _mainScreenState.emit(MainScreenState.Error)
      } finally {
        hideProgress()
      }
    }

  fun copyCurrencyRateToClipboard(currencyName: CharSequence, currencyValue: CharSequence) {
    copyCurrencyRateToClipboard.execute(currencyName, currencyValue)
  }

  private suspend fun showProgress() {
    _progress.emit(true)
    _mainScreenState.emit(MainScreenState.Loading)
  }

  suspend fun hideProgress() {
    _progress.emit(false)
  }
}
