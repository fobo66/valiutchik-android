package fobo66.exchangecourcesbelarus.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParserException

class CurrencyRatesParserImplTest {

  private lateinit var parser: fobo66.valiutchik.api.CurrencyRatesParser

  @Before
  fun setUp() {
    parser = fobo66.valiutchik.api.CurrencyRatesParserImpl()
  }

  @Test
  fun singleCurrency() {
    val testFileStream = javaClass.classLoader?.getResourceAsStream("singleCurrency.xml")!!
    val currencies = parser.parse(testFileStream)
    assertEquals(1, currencies.size)
  }

  @Test
  fun multipleCurrencies() {
    val testFileStream = javaClass.classLoader?.getResourceAsStream("multipleCurrencies.xml")!!
    val currencies = parser.parse(testFileStream)
    assertEquals(2, currencies.size)
  }

  @Test
  fun sameCurrenciesFilteredOut() {
    val testFileStream = javaClass.classLoader?.getResourceAsStream("sameCurrencies.xml")!!
    val currencies = parser.parse(testFileStream)
    assertEquals(2, currencies.size)
  }

  @Test(expected = XmlPullParserException::class)
  fun errorForIncorrectXml() {
    val testFileStream = javaClass.classLoader?.getResourceAsStream("wrongData.xml")!!
    parser.parse(testFileStream)
  }
}