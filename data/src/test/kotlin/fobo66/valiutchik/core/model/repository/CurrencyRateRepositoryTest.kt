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
import dev.fobo66.core.data.testing.fake.FakeBestCourseDataSource
import dev.fobo66.core.data.testing.fake.FakeCurrencyRatesDataSource
import dev.fobo66.core.data.testing.fake.FakeFormattingDataSource
import dev.fobo66.core.data.testing.fake.FakePersistenceDataSource
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.util.CurrencyName.DOLLAR
import fobo66.valiutchik.core.util.CurrencyName.RUB
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class CurrencyRateRepositoryTest {
  private val bestCourseDataSource = FakeBestCourseDataSource()
  private val persistenceDataSource = FakePersistenceDataSource()
  private val currencyRatesDataSource = FakeCurrencyRatesDataSource()
  private val formattingDataSource = FakeFormattingDataSource()
  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  private val currencyRateRepository: CurrencyRateRepository =
    CurrencyRateRepositoryImpl(
      bestCourseDataSource,
      persistenceDataSource,
      currencyRatesDataSource,
      formattingDataSource,
    )

  @AfterEach
  fun tearDown() {
    ioDispatcher.close()
  }

  private val now = Clock.System.now()

  @Test
  fun `load exchange rates`() {
    runTest {
      currencyRateRepository.refreshExchangeRates("Minsk", now)
    }

    assertThat(persistenceDataSource.isSaved).isTrue()
  }

  @Test
  fun `do not load exchange rates when there was an error`() {
    currencyRatesDataSource.isError = true

    runTest {
      assertThrows<CurrencyRatesLoadFailedException> {
        currencyRateRepository.refreshExchangeRates("Minsk", now)
      }
    }
  }

  @Test
  fun `normalize ruble rate`() {
    val rate = BestCourse(id = 0, currencyValue = 0.0123, currencyName = RUB)
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
