package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.models.Currency

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
internal class CurrencyListSanitizer : Sanitizer {
  override fun sanitize(list: MutableList<Currency>): List<Currency> {
    val iterator = list.iterator()
    while (iterator.hasNext()) {
      val currency = iterator.next()
      if (isInvalidEntry(currency)) {
        iterator.remove()
      }
    }
    return list
  }

  private fun isInvalidEntry(currency: Currency): Boolean {
    return currency.eurBuy == EMPTY_COURSE ||
        currency.eurSell == EMPTY_COURSE ||
        currency.rurBuy == EMPTY_COURSE ||
        currency.rurSell == EMPTY_COURSE ||
        currency.usdBuy == EMPTY_COURSE ||
        currency.usdSell == EMPTY_COURSE
  }

  companion object {
    private const val EMPTY_COURSE = "-"
  }
}