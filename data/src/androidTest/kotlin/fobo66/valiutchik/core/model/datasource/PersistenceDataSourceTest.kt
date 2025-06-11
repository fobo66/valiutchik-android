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
import com.google.common.truth.Truth.assertThat
import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import fobo66.valiutchik.core.entities.Rate
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

private const val DATE = "2025-06-11"
private const val RATE = 1.23

@SmallTest
class PersistenceDataSourceTest {
    private val db: CurrencyRatesDatabase =
        Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                CurrencyRatesDatabase::class.java
            ).build()
    private val persistenceDataSource: PersistenceDataSource = PersistenceDataSourceImpl(db)

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun saveRates() = runTest {
        val rates =
            listOf(
                Rate(0, DATE, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE),
                Rate(0, DATE, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        val savedRates = db.ratesDao().loadAllRates()
        assertThat(savedRates).hasSize(2)
    }

    @Test
    fun loadBestRates() = runTest {
        val rates =
            listOf(
                Rate(0, DATE, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE),
                Rate(0, DATE, "test", RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        db.ratesDao().resolveBestRates()
            .test {
                assertThat(awaitItem()).hasSize(10)
            }
    }
}
