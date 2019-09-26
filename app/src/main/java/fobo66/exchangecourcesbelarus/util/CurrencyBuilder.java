package fobo66.exchangecourcesbelarus.util;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 15.03.2017.
 */

public interface CurrencyBuilder {
  CurrencyBuilder with(String fieldName, String fieldValue)
      throws NoSuchFieldException, IllegalAccessException;

  Currency build();
}
