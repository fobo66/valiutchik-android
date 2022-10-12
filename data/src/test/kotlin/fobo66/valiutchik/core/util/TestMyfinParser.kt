package fobo66.valiutchik.core.util

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

class TestMyfinParser : fobo66.valiutchik.api.CurrencyRatesParser {

  private val neededTagNames by lazy {
    setOf(
      TAG_NAME_BANKNAME,
      TAG_NAME_USD_BUY,
      TAG_NAME_USD_SELL,
      TAG_NAME_EUR_BUY,
      TAG_NAME_EUR_SELL,
      TAG_NAME_RUR_BUY,
      TAG_NAME_RUR_SELL
    )
  }

  override fun parse(inputStream: InputStream): Set<fobo66.valiutchik.api.Currency> {
    val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    val documentBuilder = dbFactory.newDocumentBuilder()
    val document = documentBuilder.parse(inputStream)
    document.normalizeDocument()

    return readCurrencies(document)
  }

  private fun readCurrencies(document: Document): Set<fobo66.valiutchik.api.Currency> {
    val currencies = mutableSetOf<fobo66.valiutchik.api.Currency>()

    val nodes = document.getElementsByTagName(ENTRY_TAG_NAME)

    for (index in 0 until nodes.length) {
      val currencyNode = nodes.item(index)

      currencies.add(readCurrency(currencyNode.childNodes))
    }

    return currencies
  }

  @Throws(IOException::class)
  private fun readCurrency(nodes: NodeList): fobo66.valiutchik.api.Currency {
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
