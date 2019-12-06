package fobo66.exchangecourcesbelarus.util.comparators

import fobo66.exchangecourcesbelarus.entities.Currency

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 13.02.2017.
 */
interface CurrencyComparator : Comparator<Currency> {
  override fun compare(lhs: Currency, rhs: Currency): Int
}
