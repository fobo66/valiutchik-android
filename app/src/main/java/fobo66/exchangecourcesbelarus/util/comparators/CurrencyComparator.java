package fobo66.exchangecourcesbelarus.util.comparators;

import java.util.Comparator;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 13.02.2017.
 */

public interface CurrencyComparator extends Comparator<Currency> {
    int compare(Currency o1, Currency o2);
}
