package fobo66.exchangecourcesbelarus.util.comparators;

import fobo66.exchangecourcesbelarus.entities.Currency;

/**
 * Created by fobo66 on 31.03.2016.
 */
public class EurBuyComparator implements CurrencyComparator {
  @Override public int compare(Currency lhs, Currency rhs) {
    return rhs.eurBuy.compareToIgnoreCase(lhs.eurBuy);
  }
}

