package fobo66.exchangecourcesbelarus.util.comparators

import fobo66.exchangecourcesbelarus.entities.Currency

class UsdSellComparator : CurrencyComparator {
  override fun compare(lhs: Currency, rhs: Currency): Int {
    return rhs.usdSell.compareTo(lhs.usdSell, ignoreCase = true)
  }
}