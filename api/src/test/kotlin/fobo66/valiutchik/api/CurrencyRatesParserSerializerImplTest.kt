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

import java.io.InputStream
import kotlin.test.assertFails
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CurrencyRatesParserSerializerImplTest {
    private val parser: CurrencyRatesParser = CurrencyRatesParserSerializerImpl()

    @Test
    fun singleCurrency() {
        val testFileStream = openTestFile("singleCurrency.xml")
        val currencies = parser.parse(testFileStream)
        assertEquals(1, currencies.size)
    }

    @Test
    fun multipleCurrencies() {
        val testFileStream = openTestFile("multipleCurrencies.xml")
        val currencies = parser.parse(testFileStream)
        assertEquals(2, currencies.size)
    }

    @Test
    fun sameCurrenciesFilteredOut() {
        val testFileStream = openTestFile("sameCurrencies.xml")
        val currencies = parser.parse(testFileStream)
        assertEquals(2, currencies.size)
    }

    @Test
    fun errorForIncorrectXml() {
        val testFileStream = openTestFile("wrongData.xml")
        assertFails {
            parser.parse(testFileStream)
        }
    }

    private fun openTestFile(fileName: String): InputStream =
        javaClass.classLoader?.getResourceAsStream(fileName)!!
}
