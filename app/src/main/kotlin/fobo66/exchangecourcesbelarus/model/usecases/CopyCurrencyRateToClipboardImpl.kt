package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.valiutchik.core.model.repository.ClipboardRepository
import fobo66.valiutchik.domain.usecases.CopyCurrencyRateToClipboard
import javax.inject.Inject

class CopyCurrencyRateToClipboardImpl @Inject constructor(
  private val clipboardRepository: ClipboardRepository
) : CopyCurrencyRateToClipboard {
  override fun execute(currencyName: CharSequence, currencyValue: CharSequence) {
    clipboardRepository.copyToClipboard(currencyName, currencyValue)
  }
}
