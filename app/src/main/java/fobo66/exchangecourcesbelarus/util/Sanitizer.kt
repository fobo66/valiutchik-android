package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.models.Currency

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
internal interface Sanitizer {
  fun sanitize(list: MutableList<Currency>): List<Currency>
}