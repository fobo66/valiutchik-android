package fobo66.exchangecourcesbelarus.util.comparators

import fobo66.exchangecourcesbelarus.entities.Currency

/**
 * Created by fobo66 on 31.03.2016.
 */
class RurBuyComparator : CurrencyComparator {
  override fun compare(lhs: Currency, rhs: Currency): Int {
    return rhs.rurBuy.compareTo(lhs.rurBuy, ignoreCase = true)
  }
}
