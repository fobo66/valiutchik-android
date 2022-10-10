package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.model.repository.ClipboardRepository
import javax.inject.Inject

class CopyCurrencyRateToClipboardImpl @Inject constructor(
  private val clipboardRepository: ClipboardRepository
) : CopyCurrencyRateToClipboard {
  override fun execute(currencyName: CharSequence, currencyValue: CharSequence) {
    clipboardRepository.copyToClipboard(currencyName, currencyValue)
  }
}
