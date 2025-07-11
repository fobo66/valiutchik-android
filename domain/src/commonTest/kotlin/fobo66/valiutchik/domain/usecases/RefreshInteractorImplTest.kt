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

import app.cash.turbine.test
import dev.fobo66.domain.testing.fake.FakeForceRefreshExchangeRates
import dev.fobo66.domain.testing.fake.FakeForceRefreshExchangeRatesForDefaultCity
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class RefreshInteractorImplTest {
    private val refreshExchangeRates = FakeForceRefreshExchangeRates()
    private val refreshExchangeRatesForDefaultCity = FakeForceRefreshExchangeRatesForDefaultCity()

    private val refreshInteractor =
        RefreshInteractorImpl(refreshExchangeRates, refreshExchangeRatesForDefaultCity)

    @Test
    fun `progress updated`() = runTest {
        refreshInteractor.isRefreshInProgress.test {
            assertFalse(awaitItem())
            refreshInteractor.initiateRefresh(true)
            assertTrue(awaitItem())
            assertFalse(awaitItem())
            assertTrue(refreshExchangeRates.isRefreshed)
        }
    }

    @Test
    fun `progress updated on error`() = runTest {
        refreshExchangeRates.error = true
        refreshInteractor.isRefreshInProgress.test {
            assertFalse(awaitItem())
            refreshInteractor.initiateRefresh(true)
            assertTrue(awaitItem())
            assertFalse(awaitItem())
            assertFalse(refreshExchangeRates.isRefreshed)
        }
    }
}
