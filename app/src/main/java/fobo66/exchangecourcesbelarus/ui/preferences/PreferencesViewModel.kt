package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.entities.PreferenceScreenState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor() : ViewModel() {
  fun loadPreferences() = viewModelScope.launch {
    _state.emit(PreferenceScreenState.LoadedPreferences(emptyList()))
  }

  val state: StateFlow<PreferenceScreenState>
    get() = _state

  private val _state: MutableStateFlow<PreferenceScreenState> =
    MutableStateFlow(PreferenceScreenState.Loading)
}
