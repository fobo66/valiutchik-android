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

import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.xmlpull.v1.XmlPullParserException

@SmallTest
class CurrencyRatesParserImplTest {
    private val parser: CurrencyRatesParser = CurrencyRatesParserImpl()

    @Test
    fun singleCurrency() {
        val body = openTestFile("singleCurrency.xml")
        val currencies = parser.parse(body)
        assertEquals(1, currencies.size)
    }

    @Test
    fun multipleCurrencies() {
        val body = openTestFile("multipleCurrencies.xml")
        val currencies = parser.parse(body)
        assertEquals(2, currencies.size)
    }

    @Test
    fun sameCurrenciesFilteredOut() {
        val body = openTestFile("sameCurrencies.xml")
        val currencies = parser.parse(body)
        assertEquals(2, currencies.size)
    }

    @Test(expected = XmlPullParserException::class)
    fun errorForIncorrectXml() {
        val body = openTestFile("wrongData.xml")
        parser.parse(body)
    }

    private fun openTestFile(fileName: String): String = InstrumentationRegistry
        .getInstrumentation()
        .context.assets
        .open(fileName)
        .bufferedReader()
        .readText()
}
