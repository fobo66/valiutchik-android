package fobo66.exchangecourcesbelarus.util.comparators;

import fobo66.exchangecourcesbelarus.entities.Currency;

public class UsdSellComparator implements CurrencyComparator {
  @Override public int compare(Currency lhs, Currency rhs) {
    return rhs.usdSell.compareToIgnoreCase(lhs.usdSell);
  }
}
