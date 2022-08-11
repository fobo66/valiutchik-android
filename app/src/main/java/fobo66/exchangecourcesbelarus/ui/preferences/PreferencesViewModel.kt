package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.valiutchik.core.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.core.usecases.LoadUpdateIntervalPreference
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
  private val loadDefaultCityPreference: LoadDefaultCityPreference,
  private val loadUpdateIntervalPreference: LoadUpdateIntervalPreference
) : ViewModel() {

  val defaultCityPreference = loadDefaultCityPreference.execute()
  val updateIntervalPreference = loadUpdateIntervalPreference.execute()
}
