package fobo66.valiutchik.domain.usecases

import android.content.Intent
import fobo66.valiutchik.core.model.repository.MapRepository
import javax.inject.Inject

class FindBankOnMapImpl @Inject constructor(
  private val mapRepository: MapRepository
) : FindBankOnMap {
  override fun execute(bankName: CharSequence): Intent? = mapRepository.searchOnMap(bankName)
}
