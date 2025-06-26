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

import androidx.collection.MutableScatterMap
import androidx.collection.mutableScatterMapOf
import androidx.collection.mutableScatterSetOf
import androidx.collection.scatterSetOf
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.ExchangeRateValue
import fobo66.valiutchik.api.entity.UNDEFINED_BUY_RATE
import fobo66.valiutchik.api.entity.UNDEFINED_SELL_RATE
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.SAXException

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

    @Throws(SAXException::class, IOException::class)
    override fun parse(body: String): Set<Bank> {
        val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

        val documentBuilder = dbFactory.newDocumentBuilder()

        val document = documentBuilder.parse(body)

        document.normalizeDocument()
        return readCurrencies(document)
    }

    @Throws(SAXException::class, IOException::class)
    private fun readCurrencies(document: Document): Set<Bank> {
        val result = mutableScatterSetOf<Bank>()
        val nodes = document.getElementsByTagName(ENTRY_TAG_NAME)

        for (index in 0 until nodes.length) {
            val currencyNode = nodes.item(index)
            result.add(readCurrency(currencyNode.childNodes))
        }

        return result.asSet()
    }

    private fun readCurrency(nodes: NodeList): Bank {
        var fieldName: String
        val currencyMap = mutableScatterMapOf<String, String>()
        for (index in 0 until nodes.length) {
            val node = nodes.item(index)
            fieldName = node?.nodeName.orEmpty()
            if (neededTagNames.contains(fieldName)) {
                currencyMap[fieldName] = node?.textContent.orEmpty()
            }
        }
        return currencyMap.toCurrency()
    }

    /**
     * Builder for currency object
     */
    private fun MutableScatterMap<String, String>.toCurrency(): Bank = Bank(
        bankId = get(TAG_NAME_BANK_ID)?.toLongOrNull() ?: 0L,
        filialId = get(TAG_NAME_FILIAL_ID)?.toLongOrNull() ?: 0L,
        date = get(TAG_NAME_DATE).orEmpty(),
        filialName = get(TAG_NAME_FILIAL_NAME).orEmpty(),
        bankAddress = get(TAG_NAME_BANK_ADDRESS).orEmpty(),
        bankPhone = get(TAG_NAME_BANK_PHONE).orEmpty(),
        bankName = get(TAG_NAME_BANK_NAME).orEmpty(),
        usdBuy = ExchangeRateValue(get(TAG_NAME_USD_BUY)?.toDoubleOrNull() ?: UNDEFINED_BUY_RATE),
        usdSell = ExchangeRateValue(
            get(TAG_NAME_USD_SELL)?.toDoubleOrNull() ?: UNDEFINED_SELL_RATE
        ),
        eurBuy = ExchangeRateValue(get(TAG_NAME_EUR_BUY)?.toDoubleOrNull() ?: UNDEFINED_BUY_RATE),
        eurSell = ExchangeRateValue(
            get(TAG_NAME_EUR_SELL)?.toDoubleOrNull() ?: UNDEFINED_SELL_RATE
        ),
        rubBuy = ExchangeRateValue(get(TAG_NAME_RUR_BUY)?.toDoubleOrNull() ?: UNDEFINED_BUY_RATE),
        rubSell = ExchangeRateValue(
            get(TAG_NAME_RUR_SELL)?.toDoubleOrNull() ?: UNDEFINED_SELL_RATE
        ),
        plnBuy = ExchangeRateValue(get(TAG_NAME_PLN_BUY)?.toDoubleOrNull() ?: UNDEFINED_BUY_RATE),
        plnSell = ExchangeRateValue(
            get(TAG_NAME_PLN_SELL)?.toDoubleOrNull() ?: UNDEFINED_SELL_RATE
        ),
        uahBuy = ExchangeRateValue(get(TAG_NAME_UAH_BUY)?.toDoubleOrNull() ?: UNDEFINED_BUY_RATE),
        uahSell = ExchangeRateValue(
            get(TAG_NAME_UAH_SELL)?.toDoubleOrNull() ?: UNDEFINED_SELL_RATE
        ),
        conversionBuy = get(TAG_NAME_EURUSD_BUY).orEmpty(),
        conversionSell = get(TAG_NAME_EURUSD_SELL).orEmpty()
    )
}
