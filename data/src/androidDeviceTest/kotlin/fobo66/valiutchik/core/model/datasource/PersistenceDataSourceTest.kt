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

package fobo66.valiutchik.core.model.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Correspondence
import com.google.common.truth.Truth.assertThat
import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.Rate
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val RATE = 1.23456
private const val DEFAULT_BEST_RATES_COUNT = 10

@SmallTest
class PersistenceDataSourceTest {
    private val db: CurrencyRatesDatabase =
        Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                CurrencyRatesDatabase::class.java
            ).build()
    private val persistenceDataSource: PersistenceDataSource = PersistenceDataSourceImpl(db)

    @OptIn(ExperimentalTime::class)
    private val date =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun saveRates() = runTest {
        val rates =
            listOf(
                Rate(0, date, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE),
                Rate(0, date, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        val savedRates = db.ratesDao().loadAllRates()
        assertThat(savedRates).hasSize(2)
    }

    @Test
    fun loadBestRates() = runTest {
        val rates =
            listOf(
                Rate(0, date, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE),
                Rate(0, date, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        db.ratesDao().resolveBestRates()
            .test {
                assertThat(awaitItem()).hasSize(DEFAULT_BEST_RATES_COUNT)
            }
    }

    @Test
    fun someBestRatesAreMissing() = runTest {
        val rates =
            listOf(
                Rate(0, date, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, 0.0, RATE, RATE),
                Rate(0, date, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, 0.0, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        db.ratesDao().resolveBestRates()
            .test {
                assertThat(
                    awaitItem()
                ).comparingElementsUsing(
                    Correspondence.transforming(BestCourse::currencyValue, "Currency value")
                ).contains(0.0)
            }
    }
}
