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

package fobo66.valiutchik.domain.usecases

import dev.fobo66.core.data.testing.fake.FakeCurrencyRateRepository
import dev.fobo66.core.data.testing.fake.FakeLocationRepository
import dev.fobo66.core.data.testing.fake.FakePreferenceRepository
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class ForceRefreshExchangeRatesTest {
    private val locationRepository = FakeLocationRepository()
    private val currencyRateRepository = FakeCurrencyRateRepository()
    private val preferenceRepository = FakePreferenceRepository()

    private val refreshExchangeRates: ForceRefreshExchangeRates =
        ForceRefreshExchangeRatesImpl(
            locationRepository,
            currencyRateRepository,
            preferenceRepository
        )

    @Test
    fun `refresh exchange rates`() = runTest {
        refreshExchangeRates.execute()
        assertTrue(currencyRateRepository.isRefreshed)
    }

    @Test
    fun `resolve location for recent exchange rates`() = runTest {
        refreshExchangeRates.execute()
        assertTrue(locationRepository.isResolved)
    }
}
