package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.entities.Currency
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */
internal interface CurrencyCourseParser {
  @Throws(XmlPullParserException::class, IOException::class)
  fun parse(inputStream: InputStream): List<Currency>
}