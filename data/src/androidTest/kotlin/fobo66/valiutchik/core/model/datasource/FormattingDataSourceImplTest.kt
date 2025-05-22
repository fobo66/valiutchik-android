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
import java.util.Locale
import org.junit.Test

private const val BANK_NAME = "Приорбанк" // taken from API

@SmallTest
class FormattingDataSourceImplTest {
    private val bankNameNormalizer = FakeBankNameNormalizer()

    @Test
    fun formatCurrency() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(Locale.US, bankNameNormalizer)
        val rate = formattingDataSource.formatCurrencyValue(1.23)
        assertThat(rate).isEqualTo("BYN 1.23")
    }

    @Test
    fun formatLongCurrency() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(Locale.US, bankNameNormalizer)
        val rate = formattingDataSource.formatCurrencyValue(1.234567890)
        assertThat(rate).isEqualTo("BYN 1.23")
    }

    @Test
    fun transliterateToDefaultLocale() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(Locale.US, bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun transliterateToRandom() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(Locale.SIMPLIFIED_CHINESE, bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun passThrough() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(Locale.of("ru"), bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo(BANK_NAME)
    }

    @Test
    fun transliterateToBelarusianLocale() {
        val formattingDataSource: FormattingDataSource =
            FormattingDataSourceImpl(Locale.of("be"), bankNameNormalizer)
        val result = formattingDataSource.formatBankName(BANK_NAME)
        assertThat(result).isEqualTo("Прыорбанк")
    }
}
