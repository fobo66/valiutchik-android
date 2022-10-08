package fobo66.valiutchik.api

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
