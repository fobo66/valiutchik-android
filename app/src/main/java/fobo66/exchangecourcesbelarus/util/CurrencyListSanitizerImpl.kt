package fobo66.exchangecourcesbelarus.util

import fobo66.valiutchik.core.entities.Currency
import javax.inject.Inject
import javax.inject.Singleton

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
@Singleton
class CurrencyListSanitizerImpl @Inject constructor() : CurrencyListSanitizer {

  override fun isInvalidEntry(currency: Currency): Boolean {
    return isCurrencyRateValueInvalid(currency.eurBuy) ||
      isCurrencyRateValueInvalid(currency.eurSell) ||
      isCurrencyRateValueInvalid(currency.rurBuy) ||
      isCurrencyRateValueInvalid(currency.rurSell) ||
      isCurrencyRateValueInvalid(currency.usdBuy) ||
      isCurrencyRateValueInvalid(currency.usdSell)
  }

  private fun isCurrencyRateValueInvalid(value: String) =
    value.isEmpty() || value == EMPTY_COURSE

  companion object {
    private const val EMPTY_COURSE = "-"
  }
}