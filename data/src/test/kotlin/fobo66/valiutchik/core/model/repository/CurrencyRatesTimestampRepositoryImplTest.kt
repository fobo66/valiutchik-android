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
import dev.fobo66.core.data.testing.fake.FakePreferenceDataSource
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class CurrencyRatesTimestampRepositoryImplTest {
  private val preferencesDataSource = FakePreferenceDataSource()
  private val now = Clock.System.now()
  private val currencyRatesTimestampRepository: CurrencyRatesTimestampRepository =
    CurrencyRatesTimestampRepositoryImpl(preferencesDataSource)

  @Test
  fun `no timestamp - need to update`() {
    preferencesDataSource.string = ""

    runTest {
      assertThat(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f)).isTrue()
    }
  }

  @Test
  fun `now timestamp - need to update`() {
    preferencesDataSource.string = now.toLocalDateTime(TimeZone.currentSystemDefault()).toString()

    runTest {
      assertThat(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f)).isTrue()
    }
  }

  @Test
  fun `timestamp is old - need to update`() {
    preferencesDataSource.string =
      (now - 1.days).toLocalDateTime(TimeZone.currentSystemDefault()).toString()

    runTest {
      assertThat(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 3.0f)).isTrue()
    }
  }

  @Test
  fun `timestamp slightly in the past - no need to update`() {
    preferencesDataSource.string =
      (now - 1.hours).toLocalDateTime(TimeZone.currentSystemDefault()).toString()

    runTest {
      assertThat(
        currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(
          now,
          3.0f,
        ),
      ).isFalse()
    }
  }

  @Test
  fun `timestamp on the limit - no need to update`() {
    preferencesDataSource.string =
      (now - 3.hours).toLocalDateTime(TimeZone.currentSystemDefault()).toString()

    runTest {
      assertThat(
        currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(
          now,
          3.0f,
        ),
      ).isFalse()
    }
  }

  @Test
  fun `timestamp above customized limit - need to update`() {
    preferencesDataSource.string =
      (now - 3.hours).toLocalDateTime(TimeZone.currentSystemDefault()).toString()

    runTest {
      assertThat(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now, 2.0f)).isTrue()
    }
  }

  @Test
  fun `timestamp in the future - no need to update`() {
    preferencesDataSource.string =
      (now + 1.hours).toLocalDateTime(TimeZone.currentSystemDefault()).toString()

    runTest {
      assertThat(
        currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(
          now,
          3.0f,
        ),
      ).isFalse()
    }
  }
}
