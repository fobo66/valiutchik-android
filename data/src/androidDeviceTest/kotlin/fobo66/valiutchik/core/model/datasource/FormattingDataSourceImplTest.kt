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

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import fobo66.valiutchik.core.util.CURRENCY_NAME_US_DOLLAR
import kotlin.test.Test

@SmallTest
class FormattingDataSourceImplTest {

    private val formattingDataSource: FormattingDataSource = FormattingDataSourceImpl()

    @Test
    fun formatCurrency() {
        val rate = formattingDataSource.formatCurrencyValue(RAW_RATE, TAG)
        assertThat(rate).isEqualTo(RATE)
    }

    @Test
    fun formatCurrencyName() {
        val name = formattingDataSource.formatCurrencyName(CURRENCY_NAME_US_DOLLAR, 1, TAG)
        assertThat(name).isEqualTo(CURRENCY_NAME)
    }

    @Test
    fun formatCurrencyNamePlural() {
        val name = formattingDataSource.formatCurrencyName(CURRENCY_NAME_US_DOLLAR, 10, TAG)
        assertThat(name).isEqualTo(CURRENCY_NAME_PLURAL)
    }

    @Test
    fun formatCurrencySymbol() {
        val name = formattingDataSource.formatCurrencySymbol(CURRENCY_NAME_US_DOLLAR, TAG)
        assertThat(name).isEqualTo("$")
    }

    @Test
    fun formatLongCurrency() {
        val rate = formattingDataSource.formatCurrencyValue(LONG_RATE, TAG)
        assertThat(rate).isEqualTo(RATE)
    }

    @Test
    fun transliterateToDefaultLocale() {
        val result = formattingDataSource.formatBankName(BANK_NAME, TAG)
        assertThat(result).isEqualTo(TRANSLITERATED_BANK_NAME)
    }

    @Test
    fun transliterateToRandom() {
        val result = formattingDataSource.formatBankName(BANK_NAME, TAG)
        assertThat(result).isEqualTo(TRANSLITERATED_BANK_NAME)
    }

    @Test
    fun passThrough() {
        val result = formattingDataSource.formatBankName(BANK_NAME, PASSTHROUGH_TAG)
        assertThat(result).isEqualTo(BANK_NAME)
    }

    @Test
    fun emptyName() {
        val result = formattingDataSource.formatBankName("", TAG)
        assertThat(result).isEmpty()
    }

    @Test
    fun transliterateToBelarusianLocale() {
        val result = formattingDataSource.formatBankName(BANK_NAME, BELARUSIAN_TAG)
        assertThat(result).isEqualTo(BELARUSIFIED_BANK_NAME)
    }
}
