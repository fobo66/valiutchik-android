package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.ui.STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.domain.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.domain.usecases.UpdateUpdateIntervalPreference
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
  loadDefaultCityPreference: LoadDefaultCityPreference,
  loadUpdateIntervalPreference: LoadUpdateIntervalPreference,
  private val updateDefaultCityPreference: UpdateDefaultCityPreference,
  private val updateUpdateIntervalPreference: UpdateUpdateIntervalPreference
) : ViewModel() {

  val defaultCityPreference = loadDefaultCityPreference.execute()
    .stateIn(
      viewModelScope,
      started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
      initialValue = ""
    )
  val updateIntervalPreference = loadUpdateIntervalPreference.execute()
    .stateIn(
      viewModelScope,
      started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
      initialValue = 0.0f
    )

  fun updateDefaultCity(newDefaultCity: String) = viewModelScope.launch {
    updateDefaultCityPreference.execute(newDefaultCity)
  }

  fun updateUpdateInterval(newUpdateInterval: Float) = viewModelScope.launch {
    updateUpdateIntervalPreference.execute(newUpdateInterval)
  }
}
