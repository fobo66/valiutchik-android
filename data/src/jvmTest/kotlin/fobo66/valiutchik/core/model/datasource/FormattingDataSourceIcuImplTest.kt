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

package fobo66.valiutchik.core.model.datasource

import com.google.common.truth.Truth.assertThat
import com.ibm.icu.util.ULocale
import fobo66.valiutchik.core.util.BankNameNormalizerImpl
import kotlin.test.Test

private const val BANK_NAME = "Приорбанк" // taken from API

class FormattingDataSourceIcuImplTest {
    private val bankNameNormalizer = BankNameNormalizerImpl()

    @Test
    fun formatCurrency() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceIcuImpl(ULocale.US, bankNameNormalizer)
        val rate = formattingDataSource.formatCurrencyValue(1.23)
        assertThat(rate).isEqualTo("BYN 1.23")
    }

    @Test
    fun formatLongCurrency() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceIcuImpl(ULocale.US, bankNameNormalizer)
        val rate = formattingDataSource.formatCurrencyValue(1.234567890)
        assertThat(rate).isEqualTo("BYN 1.23")
    }

    @Test
    fun transliterateToDefaultLocale() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceIcuImpl(ULocale.US, bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun transliterateToRandom() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceIcuImpl(ULocale.SIMPLIFIED_CHINESE, bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun passThrough() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceIcuImpl(ULocale.forLanguageTag(LANG_RU), bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo(BANK_NAME)
    }

    @Test
    fun transliterateToBelarusianLocale() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceIcuImpl(ULocale.forLanguageTag(LANG_BELARUSIAN), bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Прыорбанк")
    }
}
