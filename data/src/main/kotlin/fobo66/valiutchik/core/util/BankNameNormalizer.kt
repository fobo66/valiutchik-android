package fobo66.valiutchik.core.util

import javax.inject.Inject

class BankNameNormalizer @Inject constructor() {

  private val quotes = "\"«»"

  fun normalize(bankName: String): String {
    val startTypographicalQuotePosition = bankName.indexOfFirst { it == '«' }
    val startQuotePosition = bankName.indexOfFirst { it == '\"' }

    if (startQuotePosition == -1 && startTypographicalQuotePosition == -1) {
      return bankName
    }

    val canonicalBankName = if (startTypographicalQuotePosition == -1 ||
      (startQuotePosition in 1 until startTypographicalQuotePosition)
    ) {
      val endQuotePosition = bankName.indexOf('\"', startQuotePosition + 1)
      bankName.substring(startQuotePosition + 1, endQuotePosition)
    } else {
      val endQuotePosition = bankName.indexOfFirst { it == '»' }
      bankName.substring(startTypographicalQuotePosition + 1, endQuotePosition)
    }

    return canonicalBankName.filterNot { quotes.contains(it) }
  }
}
