package fobo66.exchangecourcesbelarus.util

import javax.inject.Inject

class BankNameNormalizer @Inject constructor() {

  fun normalize(bankName: String): String {
    val startTypographicalQuotePosition = bankName.indexOfFirst {  it == '«' }
    val startQuotePosition = bankName.indexOfFirst { it == '\"' }

    if (startQuotePosition == -1 && startTypographicalQuotePosition == -1) {
      return bankName
    }

    if (startTypographicalQuotePosition == -1 || (startQuotePosition in 1 until startTypographicalQuotePosition)) {
      val endQuotePosition = bankName.indexOf('\"', startQuotePosition + 1)
      return bankName.substring(startQuotePosition + 1, endQuotePosition)
    }

    val endQuotePosition = bankName.indexOfFirst { it == '»' }
    return bankName.substring(startTypographicalQuotePosition + 1, endQuotePosition)
  }
}