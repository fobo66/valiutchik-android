package fobo66.exchangecourcesbelarus.util.comparators;

import fobo66.exchangecourcesbelarus.models.Currency;

public class USDSellComparator implements CurrencyComparator {
    @Override
    public int compare(Currency lhs, Currency rhs) {
        return rhs.usdSell.compareToIgnoreCase(lhs.usdSell);
    }
}
