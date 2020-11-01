package fobo66.exchangecourcesbelarus.model

import fobo66.valiutchik.core.entities.Currency
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
interface CurrencyRatesParser {
  @Throws(XmlPullParserException::class, IOException::class)
  fun parse(inputStream: InputStream): Set<Currency>
}