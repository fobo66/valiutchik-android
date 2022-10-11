package fobo66.valiutchik.api

import java.io.InputStream

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
interface CurrencyRatesParser {
  fun parse(inputStream: InputStream): Set<Currency>
}