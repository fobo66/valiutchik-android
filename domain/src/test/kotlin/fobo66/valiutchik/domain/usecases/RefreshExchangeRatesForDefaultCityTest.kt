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

package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.domain.fake.FakeCurrencyRateRepository
import fobo66.valiutchik.domain.fake.FakeCurrencyRatesTimestampRepository
import fobo66.valiutchik.domain.fake.FakePreferenceRepository
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RefreshExchangeRatesForDefaultCityTest {
  private val timestampRepository = FakeCurrencyRatesTimestampRepository()
  private val currencyRateRepository = FakeCurrencyRateRepository()
  private val preferenceRepository = FakePreferenceRepository()

  private val now = LocalDateTime.now()

  private val refreshExchangeRates: RefreshExchangeRatesForDefaultCity =
    RefreshExchangeRatesForDefaultCityImpl(
      timestampRepository,
      currencyRateRepository,
      preferenceRepository
    )

  @Test
  fun `refresh exchange rates`() = runTest {
    refreshExchangeRates.execute(now)
    assertTrue(currencyRateRepository.isRefreshed)
    assertTrue(timestampRepository.isSaveTimestampCalled)
  }

  @Test
  fun `do not refresh recent exchange rates`() = runTest {
    timestampRepository.isNeededToUpdateCurrencyRates = false

    refreshExchangeRates.execute(now)
    assertFalse(currencyRateRepository.isRefreshed)
  }
}
