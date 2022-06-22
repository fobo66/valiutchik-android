package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.entities.PreferenceScreenState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class PreferencesViewModel @Inject constructor() : ViewModel() {
  val state: StateFlow<PreferenceScreenState>
    get() = _state

  private val _state: MutableStateFlow<PreferenceScreenState> =
    MutableStateFlow(PreferenceScreenState.Loading)
}
