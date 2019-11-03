package fobo66.exchangecourcesbelarus.util.comparators;

import fobo66.exchangecourcesbelarus.entities.Currency;
import java.util.Comparator;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 13.02.2017.
 */

public interface CurrencyComparator extends Comparator<Currency> {
  int compare(Currency o1, Currency o2);
}
