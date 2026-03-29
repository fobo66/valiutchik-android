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

import dev.fobo66.core.data.testing.fake.FakeApiDataSource
import dev.fobo66.core.data.testing.fake.FakePersistenceDataSource
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

private const val CITY = "test"

@ExperimentalCoroutinesApi
class CurrencyRateRepositoryTest {
    private val persistenceDataSource = FakePersistenceDataSource()
    private val currencyRatesDataSource = FakeApiDataSource()

    private val currencyRateRepository: CurrencyRateRepository =
        CurrencyRateRepositoryImpl(
            persistenceDataSource,
            currencyRatesDataSource
        )

    @Test
    fun `load exchange rates`() = runTest {
        currencyRateRepository.refreshExchangeRates(CITY)

        assertTrue(persistenceDataSource.isSaved)
    }

    @Test
    fun `do not load exchange rates when there was an error`() = runTest {
        currencyRatesDataSource.isError = true

        assertFails {
            currencyRateRepository.refreshExchangeRates(CITY)
        }
    }
}
