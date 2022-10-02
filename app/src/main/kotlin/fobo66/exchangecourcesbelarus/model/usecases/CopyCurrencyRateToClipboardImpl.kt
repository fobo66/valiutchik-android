package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.repository.ClipboardRepository
import fobo66.valiutchik.core.usecases.CopyCurrencyRateToClipboard
import javax.inject.Inject

class CopyCurrencyRateToClipboardImpl @Inject constructor(
  private val clipboardRepository: ClipboardRepository
) : CopyCurrencyRateToClipboard {
  override fun execute(currencyName: CharSequence, currencyValue: CharSequence) {
    clipboardRepository.copyToClipboard(currencyName, currencyValue)
  }
}
