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

package fobo66.valiutchik.core.model.repository

import dev.fobo66.core.data.testing.fake.FakeFormattingDataSource
import dev.fobo66.core.data.testing.fake.FakeLocaleDataSource
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.util.CURRENCY_NAME_HRYVNIA
import fobo66.valiutchik.core.util.CURRENCY_NAME_US_DOLLAR
import kotlin.test.Test
import kotlin.test.assertEquals

private const val RATE = 1.23
private const val LOW_RATE = 0.0123
private const val FORMATTED_RATE = "1.23"
private const val TAG = "be-BY"

class LocaleRepositoryTest {
    private val formattingDataSource = FakeFormattingDataSource()
    private val localeDataSource = FakeLocaleDataSource()
    private val localeRepository: LocaleRepository =
        LocaleRepositoryImpl(formattingDataSource, localeDataSource)

    @Test
    fun `normalize hryvnia rate`() {
        val rate = BestCourse(
            bankName = "test",
            currencyValue = LOW_RATE,
            currencyName = CURRENCY_NAME_HRYVNIA,
            currencyId = 0,
            multiplier = 100
        )
        val result = localeRepository.formatRate(rate, TAG)
        assertEquals(FORMATTED_RATE, result)
    }

    @Test
    fun `do not normalize dollar rate`() {
        val rate = BestCourse(
            bankName = "test",
            currencyValue = RATE,
            currencyName = CURRENCY_NAME_US_DOLLAR,
            currencyId = 0,
            multiplier = 1
        )
        val result = localeRepository.formatRate(rate, TAG)
        assertEquals(FORMATTED_RATE, result)
    }
}
