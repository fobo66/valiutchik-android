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

import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyRatesParserSaxImplTest {
    private val parser: CurrencyRatesParser = CurrencyRatesParserSaxImpl()

    @Test
    fun `single currency`() {
        val testBody = openTestFile("singleCurrency.xml")
        val currencies = parser.parse(testBody)
        assertEquals(1, currencies.size)
    }

    @Test
    fun `multiple currencies`() {
        val testBody = openTestFile("multipleCurrencies.xml")
        val currencies = parser.parse(testBody)
        assertEquals(2, currencies.size)
    }

    @Test
    fun `same currencies filtered out`() {
        val testBody = openTestFile("sameCurrencies.xml")
        val currencies = parser.parse(testBody)
        assertEquals(2, currencies.size)
    }

    @Test
    fun `no currencies for incorrect xml`() {
        val testBody = openTestFile("wrongData.xml")
        val currencies = parser.parse(testBody)
        assertEquals(0, currencies.size)
    }

    private fun openTestFile(fileName: String): String =
        javaClass.classLoader?.getResourceAsStream(fileName)?.bufferedReader()
            ?.readText().orEmpty()
}
