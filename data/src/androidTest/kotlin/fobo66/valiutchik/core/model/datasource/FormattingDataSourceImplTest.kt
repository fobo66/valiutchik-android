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
import fobo66.valiutchik.core.fake.FakeBankNameNormalizer
import java.util.Locale
import org.junit.Test

@SmallTest
class FormattingDataSourceImplTest {
    private val locale = Locale.US
    private val bankNameNormalizer = FakeBankNameNormalizer()
    private val formattingDataSource: FormattingDataSource =
        FormattingDataSourceImpl(locale, bankNameNormalizer)

    @Test
    fun formatCurrency() {
        val rate = formattingDataSource.formatCurrencyValue(1.23)
        assertThat(rate).isEqualTo("BYN 1.23")
    }

    @Test
    fun formatLongCurrency() {
        val rate = formattingDataSource.formatCurrencyValue(1.234567890)
        assertThat(rate).isEqualTo("BYN 1.23")
    }
}
