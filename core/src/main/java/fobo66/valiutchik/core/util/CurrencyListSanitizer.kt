package fobo66.valiutchik.core.util

import fobo66.valiutchik.core.entities.Currency

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
interface CurrencyListSanitizer {
  fun sanitize(currencies: Set<Currency>): List<Currency>
}