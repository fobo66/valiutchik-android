package fobo66.exchangecourcesbelarus.model

import android.util.Xml
import fobo66.exchangecourcesbelarus.entities.Currency
import fobo66.exchangecourcesbelarus.util.CurrencyBuilder
import fobo66.exchangecourcesbelarus.util.CurrencyBuilderImpl
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.Arrays
import javax.inject.Inject

/**
 * XML parser for [MyFIN](myfin.by) feed
 *
 * Created by fobo66 on 16.08.2015.
 */
class MyfinParser @Inject constructor() : CurrencyRatesParser {
  private val namespace: String? = null
  private val neededTagNames: List<String> = ArrayList(
    Arrays.asList(
      "bankname", "usd_buy", "usd_sell", "eur_buy", "eur_sell", "rur_buy",
      "rur_sell"
    )
  )

  @Throws(XmlPullParserException::class, IOException::class)
  override fun parse(inputStream: InputStream): List<Currency> {
    val parser = Xml.newPullParser()
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
    parser.setInput(inputStream, "utf-8")
    parser.nextTag()
    return readFeed(parser)
  }

  @Throws(XmlPullParserException::class, IOException::class)
  private fun readFeed(parser: XmlPullParser): List<Currency> {
    val entries: MutableList<Currency> = ArrayList()
    var entry: Currency
    parser.require(XmlPullParser.START_TAG, namespace, "root")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }
      val name = parser.name
      if (name == "bank") {
        entry = readEntry(parser)
        entries.add(entry)
      } else {
        skip(parser)
      }
    }
    return entries
  }

  @Throws(XmlPullParserException::class, IOException::class)
  private fun readEntry(parser: XmlPullParser): Currency {
    parser.require(XmlPullParser.START_TAG, namespace, "bank")
    var fieldName: String
    var currencyBuilder: CurrencyBuilder = CurrencyBuilderImpl()
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }
      fieldName = parser.name
      if (isTagNeeded(fieldName)) {
        try {
          currencyBuilder = currencyBuilder.with(fieldName, readTag(parser, fieldName))
        } catch (e: Exception) {
          e.printStackTrace()
        }
      } else {
        skip(parser)
      }
    }
    return currencyBuilder.build()
  }

  private fun isTagNeeded(tagName: String): Boolean {
    return neededTagNames.contains(tagName)
  }

  @Throws(IOException::class, XmlPullParserException::class)
  private fun readTag(parser: XmlPullParser, tagName: String): String {
    parser.require(XmlPullParser.START_TAG, namespace, tagName)
    val param = readText(parser)
    parser.require(XmlPullParser.END_TAG, namespace, tagName)
    return param
  }

  @Throws(IOException::class, XmlPullParserException::class)
  private fun readText(parser: XmlPullParser): String {
    var result = ""
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.text
      parser.nextTag()
    }
    return result
  }

  @Throws(XmlPullParserException::class, IOException::class)
  private fun skip(parser: XmlPullParser) {
    check(parser.eventType == XmlPullParser.START_TAG)
    var depth = 1
    while (depth != 0) {
      when (parser.next()) {
        XmlPullParser.END_TAG -> depth--
        XmlPullParser.START_TAG -> depth++
      }
    }
  }
}