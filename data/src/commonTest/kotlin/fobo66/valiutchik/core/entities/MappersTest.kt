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

package fobo66.valiutchik.core.entities

import dev.fobo66.core.data.testing.fake.DATE
import dev.fobo66.core.data.testing.fake.ID
import dev.fobo66.core.data.testing.fake.RATE
import dev.fobo66.core.data.testing.fake.RAW_DATE
import dev.fobo66.core.data.testing.fake.buildBank
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

class MappersTest {
    @Test
    fun `ids are concatenated`() {
        val rate = buildBank().toRate(LocalDate.Formats.ISO)
        assertEquals(11L, rate.id)
    }

    @Test
    fun `ids have different digit counts`() {
        val rate = buildBank(
            bankId = 12L,
            filialId = 3L
        ).toRate(LocalDate.Formats.ISO)
        assertEquals(123L, rate.id)
    }

    @Test
    fun `primary id is zero`() {
        val rate = buildBank(
            bankId = 0L
        ).toRate(LocalDate.Formats.ISO)
        assertEquals(ID, rate.id)
    }

    @Test
    fun `secondary id is zero`() {
        val rate = buildBank(
            bankId = 12L,
            filialId = 0L
        ).toRate(LocalDate.Formats.ISO)
        assertEquals(120L, rate.id)
    }

    @Test
    fun `date is parsed by format`() {
        val rate = buildBank(
            date = RAW_DATE
        ).toRate(
            LocalDate.Format {
                day()
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
        assertEquals(DATE, rate.date)
    }

    @Test
    fun `parse rate`() {
        val rate = buildBank().toRate(LocalDate.Formats.ISO)
        assertEquals(RATE, rate.usdBuy)
    }
}
