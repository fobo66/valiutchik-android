/*
 *    Copyright 2024 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.api

import android.util.Xml
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

const val ROOT_TAG_NAME = "root"
const val ENTRY_TAG_NAME = "bank"
const val TAG_NAME_BANKNAME = "bankname"
const val TAG_NAME_USD_BUY = "usd_buy"
const val TAG_NAME_USD_SELL = "usd_sell"
const val TAG_NAME_EUR_BUY = "eur_buy"
const val TAG_NAME_EUR_SELL = "eur_sell"
const val TAG_NAME_RUR_BUY = "rub_buy"
const val TAG_NAME_RUR_SELL = "rub_sell"
const val TAG_NAME_PLN_BUY = "pln_buy"
const val TAG_NAME_PLN_SELL = "pln_sell"
const val TAG_NAME_UAH_BUY = "uah_buy"
const val TAG_NAME_UAH_SELL = "uah_sell"

class CurrencyRatesParserImpl @Inject constructor() : CurrencyRatesParser {
  private val neededTagNames by lazy {
    setOf(
      TAG_NAME_BANKNAME,
      TAG_NAME_USD_BUY,
      TAG_NAME_USD_SELL,
      TAG_NAME_EUR_BUY,
      TAG_NAME_EUR_SELL,
      TAG_NAME_RUR_BUY,
      TAG_NAME_RUR_SELL,
      TAG_NAME_PLN_BUY,
      TAG_NAME_PLN_SELL,
      TAG_NAME_UAH_BUY,
      TAG_NAME_UAH_SELL
    )
  }

  @Throws(XmlPullParserException::class, IOException::class)
  override fun parse(inputStream: InputStream): Set<Currency> {
    val parser = Xml.newPullParser().apply {
      setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
      setInput(inputStream, "utf-8")
      nextTag()
    }
    return readCurrencies(parser)
  }

  @Throws(XmlPullParserException::class, IOException::class)
  private fun readCurrencies(parser: XmlPullParser): Set<Currency> {
    val currencies = mutableSetOf<Currency>()
    var currency: Currency
    parser.require(XmlPullParser.START_TAG, null, ROOT_TAG_NAME)
    parser.read {
      if (parser.name == ENTRY_TAG_NAME) {
        currency = readCurrency(parser)
        currencies.add(currency)
      } else {
        parser.skip()
      }
    }
    return currencies
  }

  @Throws(XmlPullParserException::class, IOException::class)
  private fun readCurrency(parser: XmlPullParser): Currency {
    parser.require(XmlPullParser.START_TAG, null, ENTRY_TAG_NAME)
    var fieldName: String
    val currencyMap = mutableMapOf<String, String>()
    parser.read {
      fieldName = parser.name
      if (isTagNeeded(fieldName)) {
        currencyMap[fieldName] = readTag(parser, fieldName)
      } else {
        parser.skip()
      }
    }
    return currencyMap.toCurrency()
  }

  private fun isTagNeeded(tagName: String): Boolean = neededTagNames.contains(tagName)

  @Throws(IOException::class, XmlPullParserException::class)
  private fun readTag(parser: XmlPullParser, tagName: String): String {
    parser.require(XmlPullParser.START_TAG, null, tagName)
    val param = readText(parser)
    parser.require(XmlPullParser.END_TAG, null, tagName)
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
  private fun XmlPullParser.skip() {
    check(eventType == XmlPullParser.START_TAG)
    var depth = 1
    while (depth != 0) {
      when (next()) {
        XmlPullParser.END_TAG -> depth--
        XmlPullParser.START_TAG -> depth++
      }
    }
  }

  private inline fun XmlPullParser.read(block: () -> Unit) {
    while (next() != XmlPullParser.END_TAG) {
      if (eventType != XmlPullParser.START_TAG) {
        continue
      }

      block()
    }
  }

  /**
   * Builder for currency object
   */
  private fun MutableMap<String, String>.toCurrency(): Currency =
    Currency(
      bankname = get(TAG_NAME_BANKNAME).orEmpty(),
      usdBuy = get(TAG_NAME_USD_BUY).orEmpty(),
      usdSell = get(TAG_NAME_USD_SELL).orEmpty(),
      eurBuy = get(TAG_NAME_EUR_BUY).orEmpty(),
      eurSell = get(TAG_NAME_EUR_SELL).orEmpty(),
      rurBuy = get(TAG_NAME_RUR_BUY).orEmpty(),
      rurSell = get(TAG_NAME_RUR_SELL).orEmpty(),
      plnBuy = get(TAG_NAME_PLN_BUY).orEmpty(),
      plnSell = get(TAG_NAME_PLN_SELL).orEmpty(),
      uahBuy = get(TAG_NAME_UAH_BUY).orEmpty(),
      uahSell = get(TAG_NAME_UAH_SELL).orEmpty()
    )
}
