package fobo66.exchangecourcesbelarus.util.comparators;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * Created by fobo66 on 31.03.2016.
 */
public class EURBuyComparator implements CurrencyComparator {
    @Override
    public int compare(Currency lhs, Currency rhs) {
        return rhs.eurBuy.compareToIgnoreCase(lhs.eurBuy);
    }
}

