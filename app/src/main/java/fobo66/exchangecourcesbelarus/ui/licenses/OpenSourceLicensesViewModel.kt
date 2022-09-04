package fobo66.exchangecourcesbelarus.ui.licenses

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.exchangecourcesbelarus.entities.LicenseItem
import fobo66.exchangecourcesbelarus.entities.LicensesState
import fobo66.valiutchik.core.usecases.LoadOpenSourceLicenses
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class OpenSourceLicensesViewModel @Inject constructor(
  loadOpenSourceLicenses: LoadOpenSourceLicenses
) : ViewModel() {
  val licensesState = loadOpenSourceLicenses.execute()
    .map {
      it.map { item ->
        LicenseItem(
          project = item.project,
          licenses = item.licenses.joinToString { license -> license.license },
          year = item.year.orEmpty(),
          authors = item.developers.joinToString(),
          url = item.url
        )
      }
    }
    .map { LicensesState(it) }
}
