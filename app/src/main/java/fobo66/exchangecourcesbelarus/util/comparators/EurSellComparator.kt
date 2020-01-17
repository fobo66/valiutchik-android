package fobo66.exchangecourcesbelarus.util.comparators

import fobo66.exchangecourcesbelarus.entities.Currency

class EurSellComparator : Comparator<Currency> {
  override fun compare(lhs: Currency, rhs: Currency): Int {
    return rhs.eurSell.compareTo(lhs.eurSell, ignoreCase = true)
  }
}
