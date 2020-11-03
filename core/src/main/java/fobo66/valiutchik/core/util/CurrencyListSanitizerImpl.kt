package fobo66.valiutchik.core.util

import fobo66.valiutchik.core.entities.Currency
import javax.inject.Inject
import javax.inject.Singleton

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
@Singleton
class CurrencyListSanitizerImpl @Inject constructor() : CurrencyListSanitizer {

  override fun isValidEntry(currency: Currency): Boolean {
    return isCurrencyRateValueValid(currency.eurBuy) ||
      isCurrencyRateValueValid(currency.eurSell) ||
      isCurrencyRateValueValid(currency.rurBuy) ||
      isCurrencyRateValueValid(currency.rurSell) ||
      isCurrencyRateValueValid(currency.usdBuy) ||
      isCurrencyRateValueValid(currency.usdSell)
  }

  private fun isCurrencyRateValueValid(value: String) =
    value.isNotEmpty() && value != EMPTY_COURSE

  companion object {
    private const val EMPTY_COURSE = "-"
  }
}