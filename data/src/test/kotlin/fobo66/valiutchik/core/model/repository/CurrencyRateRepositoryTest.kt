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

package fobo66.valiutchik.core.model.repository

import com.google.common.truth.Truth.assertThat
import dev.fobo66.core.data.testing.fake.FakeCurrencyRatesDataSource
import dev.fobo66.core.data.testing.fake.FakeFormattingDataSource
import dev.fobo66.core.data.testing.fake.FakePersistenceDataSource
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.util.CurrencyName
import fobo66.valiutchik.core.util.CurrencyName.DOLLAR
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class CurrencyRateRepositoryTest {
    private val persistenceDataSource = FakePersistenceDataSource()
    private val currencyRatesDataSource = FakeCurrencyRatesDataSource()
    private val formattingDataSource = FakeFormattingDataSource()

    private val currencyRateRepository: CurrencyRateRepository =
        CurrencyRateRepositoryImpl(
            persistenceDataSource,
            currencyRatesDataSource,
            formattingDataSource
        )

    @Test
    fun `load exchange rates`() {
        runTest {
            currencyRateRepository.refreshExchangeRates("Minsk")
        }

        assertThat(persistenceDataSource.isSaved).isTrue()
    }

    @Test
    fun `do not load exchange rates when there was an error`() {
        currencyRatesDataSource.isError = true

        runTest {
            assertThrows<CurrencyRatesLoadFailedException> {
                currencyRateRepository.refreshExchangeRates("Minsk")
            }
        }
    }

    @Test
    fun `normalize hryvnia rate`() {
        val rate = BestCourse(id = 0, currencyValue = 0.0123, currencyName = CurrencyName.UAH)
        val result = currencyRateRepository.formatRate(rate)
        assertThat(result).isEqualTo("1.23")
    }

    @Test
    fun `do not normalize dollar rate`() {
        val rate = BestCourse(id = 0, currencyValue = 1.23, currencyName = DOLLAR)
        val result = currencyRateRepository.formatRate(rate)
        assertThat(result).isEqualTo("1.23")
    }
}
