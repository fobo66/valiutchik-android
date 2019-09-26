package fobo66.exchangecourcesbelarus.util;

import java.util.List;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 14.03.2017.
 */

interface Sanitizer {
    List<Currency> sanitize(List<Currency> list);
}
