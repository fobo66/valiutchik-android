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

import androidx.collection.mutableScatterMapOf
import androidx.collection.mutableScatterSetOf
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.toCurrency
import org.kobjects.ktxml.api.EventType
import org.kobjects.ktxml.api.XmlPullParser
import org.kobjects.ktxml.mini.MiniXmlPullParser

class CurrencyRatesParserCommonImpl : CurrencyRatesParser() {
    override fun parse(body: String): Set<Bank> {
        val parser = MiniXmlPullParser(body).apply {
            nextTag()
        }
        return readCurrencies(parser)
    }

    private fun readCurrencies(parser: XmlPullParser): Set<Bank> {
        val result = mutableScatterSetOf<Bank>()
        var currency: Bank
        parser.require(EventType.START_TAG, null, ROOT_TAG_NAME)
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

    private fun readCurrency(parser: XmlPullParser): Bank {
        parser.require(EventType.START_TAG, null, ENTRY_TAG_NAME)
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

    private fun readTag(parser: XmlPullParser, tagName: String): String {
        parser.require(EventType.START_TAG, null, tagName)
        val param = readText(parser)
        parser.require(EventType.END_TAG, null, tagName)
        return param
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == EventType.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun XmlPullParser.skip() {
        check(eventType == EventType.START_TAG)
        var depth = 1
        while (depth != 0) {
            when (next()) {
                EventType.END_TAG -> depth--
                EventType.START_TAG -> depth++
                else -> continue
            }
        }
    }

    private inline fun XmlPullParser.read(block: () -> Unit) {
        while (next() != EventType.END_TAG) {
            if (eventType != EventType.START_TAG) {
                continue
            }

            block()
        }
    }
}
