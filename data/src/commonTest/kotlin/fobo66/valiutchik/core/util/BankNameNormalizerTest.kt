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

import kotlin.test.Test
import kotlin.test.assertEquals

class BankNameNormalizerTest {
    private val bankNameNormalizer: BankNameNormalizer = BankNameNormalizerImpl()

    @Test
    fun `bank name inside typographic quotes`() {
        val bankName = "ЗАО «БСБ Банк»"
        assertEquals("БСБ Банк", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `bank name inside regular quotes`() {
        val bankName = "ЗАО \"БСБ Банк\""
        assertEquals("БСБ Банк", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `bank name inside multiple quotes`() {
        val bankName = "ЗАО «Статусбанк» (бывш. \"Евроторгинвестбанк\")"
        assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `bank name inside multiple pairs of typographical quotes`() {
        val bankName = "ЗАО «Статусбанк» (бывш. «Евроторгинвестбанк»)"
        assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `bank name inside multiple pairs of regular quotes`() {
        val bankName = "ЗАО \"Статусбанк\" (бывш. \"Евроторгинвестбанк\")"
        assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `bank name with multiple nested quotes starting with typographical`() {
        val bankName = "ЗАО «Статусбанк \"Евроторгинвестбанк\"»"
        assertEquals("Статусбанк Евроторгинвестбанк", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `bank name with multiple nested quotes starting with regular`() {
        val bankName = "ЗАО \"Статусбанк «Евроторгинвестбанк»\""
        assertEquals("Статусбанк Евроторгинвестбанк", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `real bank name with nested imbalanced quotes`() {
        val bankName = "ЗАО «Банк «Решение»"
        assertEquals("Банк Решение", bankNameNormalizer.normalize(bankName))
    }

    @Test
    fun `bank name without quotes`() {
        val bankName = "Статусбанк"
        assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
    }
}
