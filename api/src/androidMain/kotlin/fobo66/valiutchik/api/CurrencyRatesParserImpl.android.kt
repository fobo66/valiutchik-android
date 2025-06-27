/*
 *    Copyright 2025 Andrey Mukamolov
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
import androidx.collection.mutableScatterMapOf
import androidx.collection.mutableScatterSetOf
import androidx.collection.scatterSetOf
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.toCurrency
import java.io.IOException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

class CurrencyRatesParserImpl : CurrencyRatesParser {
    private val neededTagNames by lazy(LazyThreadSafetyMode.NONE) {
        scatterSetOf(
            TAG_NAME_BANK_ID,
            TAG_NAME_FILIAL_ID,
            TAG_NAME_DATE,
            TAG_NAME_BANK_NAME,
            TAG_NAME_FILIAL_NAME,
            TAG_NAME_BANK_ADDRESS,
            TAG_NAME_BANK_PHONE,
            TAG_NAME_USD_BUY,
            TAG_NAME_USD_SELL,
            TAG_NAME_EUR_BUY,
            TAG_NAME_EUR_SELL,
            TAG_NAME_RUR_BUY,
            TAG_NAME_RUR_SELL,
            TAG_NAME_PLN_BUY,
            TAG_NAME_PLN_SELL,
            TAG_NAME_UAH_BUY,
            TAG_NAME_UAH_SELL,
            TAG_NAME_EURUSD_BUY,
            TAG_NAME_EURUSD_SELL
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    override fun parse(body: String): Set<Bank> = body.reader().buffered().use {
        val parser =
            Xml.newPullParser().apply {
                setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                setInput(it)
                nextTag()
            }
        return readCurrencies(parser)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readCurrencies(parser: XmlPullParser): Set<Bank> {
        val result = mutableScatterSetOf<Bank>()
        var currency: Bank
        parser.require(XmlPullParser.START_TAG, null, ROOT_TAG_NAME)
        parser.read {
            if (parser.name == ENTRY_TAG_NAME) {
                currency = readCurrency(parser)
                result.add(currency)
            } else {
                parser.skip()
            }
        }
        return result.asSet()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readCurrency(parser: XmlPullParser): Bank {
        parser.require(XmlPullParser.START_TAG, null, ENTRY_TAG_NAME)
        var fieldName: String
        val currencyMap = mutableScatterMapOf<String, String>()
        parser.read {
            fieldName = parser.name
            if (neededTagNames.contains(fieldName)) {
                currencyMap[fieldName] = readTag(parser, fieldName)
            } else {
                parser.skip()
            }
        }
        return currencyMap.toCurrency()
    }

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
}
