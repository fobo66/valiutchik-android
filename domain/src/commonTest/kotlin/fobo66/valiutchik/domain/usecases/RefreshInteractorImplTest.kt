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
import com.google.common.truth.Truth.assertThat
import dev.fobo66.domain.testing.fake.FakeForceRefreshExchangeRates
import dev.fobo66.domain.testing.fake.FakeForceRefreshExchangeRatesForDefaultCity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class RefreshInteractorImplTest {
    private val refreshExchangeRates = FakeForceRefreshExchangeRates()
    private val refreshExchangeRatesForDefaultCity = FakeForceRefreshExchangeRatesForDefaultCity()

    private val refreshInteractor =
        RefreshInteractorImpl(refreshExchangeRates, refreshExchangeRatesForDefaultCity)

    @Test
    fun `progress updated`() = runTest {
        refreshInteractor.isRefreshInProgress.test {
            assertThat(awaitItem()).isFalse()
            refreshInteractor.initiateRefresh(true)
            assertThat(awaitItem()).isTrue()
            assertThat(awaitItem()).isFalse()
            assertThat(refreshExchangeRates.isRefreshed).isTrue()
        }
    }

    @Test
    fun `progress updated on error`() = runTest {
        refreshExchangeRates.error = true
        refreshInteractor.isRefreshInProgress.test {
            assertThat(awaitItem()).isFalse()
            refreshInteractor.initiateRefresh(true)
            assertThat(awaitItem()).isTrue()
            assertThat(awaitItem()).isFalse()
            assertThat(refreshExchangeRates.isRefreshed).isFalse()
        }
    }
}
