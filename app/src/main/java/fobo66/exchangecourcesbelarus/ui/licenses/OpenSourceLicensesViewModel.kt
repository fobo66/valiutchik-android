package fobo66.exchangecourcesbelarus.ui.licenses

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fobo66.valiutchik.core.usecases.LoadOpenSourceLicenses
import javax.inject.Inject

@HiltViewModel
class OpenSourceLicensesViewModel @Inject constructor(
  loadOpenSourceLicenses: LoadOpenSourceLicenses
) : ViewModel() {
  val licenses = loadOpenSourceLicenses.execute()
}
