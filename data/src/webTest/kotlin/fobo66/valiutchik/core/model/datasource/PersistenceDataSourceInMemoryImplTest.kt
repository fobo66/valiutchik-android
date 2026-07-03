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

package fobo66.valiutchik.core.model.datasource

import app.cash.turbine.test
import dev.fobo66.valiutchik.core.db.Currency
import fobo66.valiutchik.core.util.CURRENCY_NAME_US_DOLLAR
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

private const val RATE = 1.23456
private const val ID = 1L

class PersistenceDataSourceInMemoryImplTest {
    private val persistenceDataSource: PersistenceDataSource = PersistenceDataSourceInMemoryImpl()

    @OptIn(ExperimentalTime::class)
    private val date =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

    @OptIn(ExperimentalTime::class)
    private val oldDate =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.minus(
            3,
            DateTimeUnit.MONTH
        ).toString()

    @Test
    fun `save currencies`() = runTest {
        val currencies = setOf(Currency(ID, CURRENCY_NAME_US_DOLLAR, CURRENCY_NAME_US_DOLLAR, ID))
        persistenceDataSource.saveCurrencies(currencies)
        persistenceDataSource.loadCurrencies().test {
            assertContentEquals(currencies, awaitItem())
        }
    }
}
