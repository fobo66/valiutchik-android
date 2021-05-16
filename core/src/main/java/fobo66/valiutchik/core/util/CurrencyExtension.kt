package fobo66.valiutchik.core.util

import fobo66.valiutchik.core.CurrencyName
import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUB
import fobo66.valiutchik.core.RUR
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.entities.Currency

/**
 * Method to figure out which currency will be used depends on the context
 * By default, USD value is returned
 * If I find the better way to do it, I'll rewrite it.
 */
fun Currency.resolveCurrencyBuyRate(@CurrencyName name: String): String {
  return when (name) {
    EUR -> eurBuy
    RUB, RUR -> rurBuy
    USD -> usdBuy
    else -> usdBuy
  }
}

/**
 * See above.
 */
fun Currency.resolveCurrencySellRate(@CurrencyName name: String): String {
  return when (name) {
    EUR -> eurSell
    RUB, RUR -> rurSell
    USD -> usdSell
    else -> usdSell
  }
}