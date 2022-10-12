/*
 *    Copyright 2022 Andrey Mukamolov
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

import fobo66.valiutchik.core.fake.FakePreferenceDataSource
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CurrencyRatesTimestampRepositoryImplTest {

  private val preferencesDataSource = FakePreferenceDataSource()
  private val now = LocalDateTime.now()
  private val currencyRatesTimestampRepository: CurrencyRatesTimestampRepository =
    CurrencyRatesTimestampRepositoryImpl(preferencesDataSource)

  @Test
  fun `no timestamp - need to update`() {
    preferencesDataSource.string = ""

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `now timestamp - need to update`() {
    preferencesDataSource.string = now.toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp is old - need to update`() {
    preferencesDataSource.string = now.minusDays(1).toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp slightly in the past - no need to update`() {
    preferencesDataSource.string = now.minusHours(1).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp on the limit - no need to update`() {
    preferencesDataSource.string = now.minusHours(3).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }

  @Test
  fun `timestamp above customized limit - need to update`() {
    preferencesDataSource.string = now.minusHours(3).toString()

    runTest {
      assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 2.0f))
    }
  }

  @Test
  fun `timestamp in the future - no need to update`() {
    preferencesDataSource.string = now.plusHours(1).toString()

    runTest {
      assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f))
    }
  }
}
