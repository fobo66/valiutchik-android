package fobo66.exchangecourcesbelarus.util;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 15.03.2017.
 */

public interface CurrencyBuilder {
    CurrencyBuilder with(String fieldName, String fieldValue) throws NoSuchFieldException, IllegalAccessException;
    Currency build();
}
