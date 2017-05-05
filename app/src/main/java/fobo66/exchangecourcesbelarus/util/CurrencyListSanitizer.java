package fobo66.exchangecourcesbelarus.util;

import java.util.Iterator;
import java.util.List;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 14.03.2017.
 */

class CurrencyListSanitizer implements Sanitizer {
    private static final String EMPTY_COURSE = "-";

    public List<Currency> sanitize(List<Currency> list) {
        for (Iterator<Currency> iterator = list.iterator(); iterator.hasNext(); ) {
            Currency currency = iterator.next();
            if (isInvalidEntry(currency)) {
                iterator.remove();
            }
        }
        return list;
    }

    private boolean isInvalidEntry(Currency currency) {
        return currency.eurBuy.equals(EMPTY_COURSE) ||
                currency.eurSell.equals(EMPTY_COURSE) ||
                currency.rurBuy.equals(EMPTY_COURSE) ||
                currency.rurSell.equals(EMPTY_COURSE) ||
                currency.usdBuy.equals(EMPTY_COURSE) ||
                currency.usdSell.equals(EMPTY_COURSE);
    }
}
