/*
 *    Copyright 2026 Andrey Mukamolov
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

package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.core.util.CURRENCY_NAME_US_DOLLAR
import kotlin.test.Test
import kotlin.test.assertEquals

class FormattingDataSourceWebTest {
    private val formattingDataSource: FormattingDataSource = FormattingDataSourceWebImpl()

    @Test
    fun formatCurrency() {
        val rate = formattingDataSource.formatCurrencyValue(RAW_RATE, TAG)
        assertEquals(RATE, rate)
    }

    @Test
    fun formatLongCurrency() {
        val rate = formattingDataSource.formatCurrencyValue(LONG_RATE, TAG)
        assertEquals(RATE, rate)
    }

    @Test
    fun formatCurrencyName() {
        val name = formattingDataSource.formatCurrencyName(CURRENCY_NAME_US_DOLLAR, 1, TAG)
        assertEquals(CURRENCY_NAME_US_DOLLAR.uppercase(), name)
    }

    @Test
    fun formatCurrencySymbol() {
        val name = formattingDataSource.formatCurrencySymbol(CURRENCY_NAME_US_DOLLAR, TAG)
        assertEquals("$", name)
    }

    @Test
    fun transliterateToDefaultLocale() {
        val result = formattingDataSource.formatBankName(BANK_NAME, TAG)
        assertEquals(TRANSLITERATED_BANK_NAME, result)
    }

    @Test
    fun transliterateToRandom() {
        val result = formattingDataSource.formatBankName(BANK_NAME, TAG)
        assertEquals(TRANSLITERATED_BANK_NAME, result)
    }

    @Test
    fun emptyName() {
        val result = formattingDataSource.formatBankName("", TAG)
        assertEquals("", result)
    }

    @Test
    fun passThrough() {
        val result = formattingDataSource.formatBankName(BANK_NAME, PASSTHROUGH_TAG)
        assertEquals(BANK_NAME, result)
    }

    @Test
    fun transliterateToBelarusianLocale() {
        val result = formattingDataSource.formatBankName(BANK_NAME, BELARUSIAN_TAG)
        assertEquals(BELARUSIFIED_BANK_NAME, result)
    }
}
