package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    loadDefaultCityPreference: LoadDefaultCityPreference,
    loadUpdateIntervalPreference: LoadUpdateIntervalPreference,
    private val updateDefaultCityPreference: UpdateDefaultCityPreference,
    private val updateUpdateIntervalPreference: UpdateUpdateIntervalPreference
) : ViewModel() {

  val defaultCityPreference = loadDefaultCityPreference.execute()
  val updateIntervalPreference = loadUpdateIntervalPreference.execute()

  fun updateDefaultCity(newDefaultCity: String) = viewModelScope.launch {
    updateDefaultCityPreference.execute(newDefaultCity)
  }

  fun updateUpdateInterval(newUpdateInterval: Float) = viewModelScope.launch {
    updateUpdateIntervalPreference.execute(newUpdateInterval)
  }
}
