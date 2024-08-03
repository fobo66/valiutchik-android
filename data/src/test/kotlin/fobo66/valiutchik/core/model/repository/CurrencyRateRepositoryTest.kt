/*
 *    Copyright 2024 Andrey Mukamolov
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

import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.fake.FakeBestCourseDataSource
import fobo66.valiutchik.core.fake.FakeCurrencyRatesDataSource
import fobo66.valiutchik.core.fake.FakePersistenceDataSource
import fobo66.valiutchik.core.util.BankNameNormalizer
import java.util.concurrent.Executors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class CurrencyRateRepositoryTest {

  private val bestCourseDataSource = FakeBestCourseDataSource()
  private val persistenceDataSource = FakePersistenceDataSource()
  private val currencyRatesDataSource = FakeCurrencyRatesDataSource()
  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  private val currencyRateRepository: CurrencyRateRepository = CurrencyRateRepositoryImpl(
    bestCourseDataSource,
    persistenceDataSource,
    currencyRatesDataSource,
    BankNameNormalizer()
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

    assertTrue(persistenceDataSource.isSaved)
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
}
