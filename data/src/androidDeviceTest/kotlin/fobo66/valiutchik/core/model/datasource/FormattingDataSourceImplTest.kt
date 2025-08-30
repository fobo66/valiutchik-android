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

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dev.fobo66.core.data.testing.fake.FakeBankNameNormalizer
import kotlin.test.Test

@SmallTest
class FormattingDataSourceImplTest {
    private val bankNameNormalizer = FakeBankNameNormalizer()

    @Test
    fun formatCurrency() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(bankNameNormalizer)
        val rate = formattingDataSource.formatCurrencyValue(RAW_RATE)
        assertThat(rate).isEqualTo(RATE)
    }

    @Test
    fun formatLongCurrency() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(bankNameNormalizer)
        val rate = formattingDataSource.formatCurrencyValue(LONG_RATE)
        assertThat(rate).isEqualTo(RATE)
    }

    @Test
    fun transliterateToDefaultLocale() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun transliterateToRandom() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun passThrough() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo(BANK_NAME)
    }

    @Test
    fun emptyName() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(bankNameNormalizer)
        val result = formattingDataSource.formatBankName("")
        assertThat(result).isEmpty()
    }

    @Test
    fun transliterateToBelarusianLocale() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Прыорбанк")
    }
}
