package fobo66.exchangecourcesbelarus.util;

import fobo66.exchangecourcesbelarus.entities.Currency;
import java.util.Iterator;
import java.util.List;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */

class CurrencyListSanitizer implements Sanitizer {
  private static final String EMPTY_COURSE = "-";

  @Override public List<Currency> sanitize(List<Currency> list) {
    for (Iterator<Currency> iterator = list.iterator(); iterator.hasNext(); ) {
      Currency currency = iterator.next();
      if (isInvalidEntry(currency)) {
        iterator.remove();
      }
    }
    return list;
  }

  private boolean isInvalidEntry(Currency currency) {
    return currency.eurBuy.equals(EMPTY_COURSE)
        || currency.eurSell.equals(EMPTY_COURSE)
        || currency.rurBuy.equals(EMPTY_COURSE)
        || currency.rurSell.equals(EMPTY_COURSE)
        || currency.usdBuy.equals(EMPTY_COURSE)
        || currency.usdSell.equals(EMPTY_COURSE);
  }
}
