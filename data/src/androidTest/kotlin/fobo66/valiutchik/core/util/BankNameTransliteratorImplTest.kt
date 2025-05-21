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

package fobo66.valiutchik.core.util

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import java.util.Locale
import org.junit.Test

private const val BANK_NAME = "Приорбанк" // taken from API

@SmallTest
class BankNameTransliteratorImplTest {
    private val transliterator: BankNameTransliterator = BankNameTransliteratorImpl()

    @Test
    fun transliterateToDefaultLocale() {
        val result = transliterator.transliterate(BANK_NAME, Locale.US.isO3Language)
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun transliterateToRandom() {
        val result = transliterator.transliterate(BANK_NAME, "test")
        assertThat(result).isEqualTo("Priorbank")
    }

    @Test
    fun passThrough() {
        val result = transliterator.transliterate(BANK_NAME, "rus")
        assertThat(result).isEqualTo(BANK_NAME)
    }

    @Test
    fun transliterateToBelarusianLocale() {
        val result = transliterator.transliterate(BANK_NAME, "bel")
        assertThat(result).isEqualTo("Прыорбанк")
    }
}
