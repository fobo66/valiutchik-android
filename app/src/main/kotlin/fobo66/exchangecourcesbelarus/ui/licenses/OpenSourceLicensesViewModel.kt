package fobo66.exchangecourcesbelarus.ui.licenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.entities.LicenseItem
import fobo66.exchangecourcesbelarus.entities.LicensesState
import fobo66.exchangecourcesbelarus.ui.STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS
import fobo66.valiutchik.domain.usecases.LoadOpenSourceLicenses
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class OpenSourceLicensesViewModel @Inject constructor(
  loadOpenSourceLicenses: LoadOpenSourceLicenses
) : ViewModel() {
  val licensesState = loadOpenSourceLicenses.execute()
    .map {
      it.map { item ->
        LicenseItem(
          project = item.project,
          licenses = item.licenses.joinToString(),
          year = item.year.orEmpty(),
          authors = item.developers.joinToString(),
          url = item.url
        )
      }
    }
    .map { LicensesState(it.toImmutableList()) }
    .stateIn(
      viewModelScope,
      started = SharingStarted.WhileSubscribed(STATE_FLOW_SUBSCRIBE_STOP_TIMEOUT_MS),
      initialValue = LicensesState(
        persistentListOf()
      )
    )
}
