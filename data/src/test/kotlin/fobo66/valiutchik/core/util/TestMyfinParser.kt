/*
 *    Copyright 2023 Andrey Mukamolov
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

package fobo66.valiutchik.core.util

import fobo66.valiutchik.api.Currency
import fobo66.valiutchik.api.CurrencyRatesParser
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_PLN_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_PLN_SELL
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_UAH_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_UAH_SELL
import fobo66.valiutchik.core.TAG_NAME_BANKNAME
import fobo66.valiutchik.core.TAG_NAME_EUR_BUY
import fobo66.valiutchik.core.TAG_NAME_EUR_SELL
import fobo66.valiutchik.core.TAG_NAME_RUR_BUY
import fobo66.valiutchik.core.TAG_NAME_RUR_SELL
import fobo66.valiutchik.core.TAG_NAME_USD_BUY
import fobo66.valiutchik.core.TAG_NAME_USD_SELL
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import org.w3c.dom.NodeList

class TestMyfinParser : CurrencyRatesParser {

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

  override fun parse(inputStream: InputStream): Set<Currency> {
    val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    val documentBuilder = dbFactory.newDocumentBuilder()
    val document = documentBuilder.parse(inputStream)
    document.normalizeDocument()

    return readCurrencies(document)
  }

  private fun readCurrencies(document: Document): Set<Currency> {
    val currencies = mutableSetOf<Currency>()

    val nodes = document.getElementsByTagName(ENTRY_TAG_NAME)

    for (index in 0 until nodes.length) {
      val currencyNode = nodes.item(index)

      currencies.add(readCurrency(currencyNode.childNodes))
    }

    return currencies
  }

  @Throws(IOException::class)
  private fun readCurrency(nodes: NodeList): Currency {
    var fieldName: String
    var currencyBuilder: fobo66.valiutchik.api.CurrencyBuilder =
      fobo66.valiutchik.api.CurrencyBuilderImpl()
    for (index in 0 until nodes.length) {
      val node = nodes.item(index)
      fieldName = node?.nodeName.orEmpty()
      if (isTagNeeded(fieldName)) {
        currencyBuilder = currencyBuilder.with(fieldName, node?.textContent.orEmpty())
      }
    }

    return currencyBuilder.build()
  }

  private fun isTagNeeded(tagName: String): Boolean {
    return neededTagNames.contains(tagName)
  }

  companion object {
    const val ENTRY_TAG_NAME = "bank"
  }
}
