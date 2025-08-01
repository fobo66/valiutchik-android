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

package fobo66.valiutchik.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fobo66.valiutchik.api.CurrencyRatesParserImpl
import fobo66.valiutchik.core.db.CurrencyRatesDatabase
import fobo66.valiutchik.core.entities.Rate
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSourceImpl
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Benchmark for resolving best course via SQLite
 */
@RunWith(AndroidJUnit4::class)
class BestCoursePersistenceDataSourceBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val db: CurrencyRatesDatabase =
        Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                CurrencyRatesDatabase::class.java
            ).build()

    private val parser = CurrencyRatesParserImpl()

    private val persistenceDataSource: PersistenceDataSource = PersistenceDataSourceImpl(db)

    @Before
    fun setUp() = runTest {
        val ratesResponseContent =
            InstrumentationRegistry
                .getInstrumentation()
                .context.assets
                .open("myfinFeed.xml")
                .bufferedReader()
                .readText()
        persistenceDataSource.saveRates(
            parser.parse(ratesResponseContent)
                .map {
                    Rate(
                        id = 0L,
                        date = LocalDate.now().toString(),
                        bankName = it.bankName,
                        usdBuy = it.usdBuy,
                        usdSell = it.usdSell,
                        eurBuy = it.eurBuy,
                        eurSell = it.eurSell,
                        rubBuy = it.rubBuy,
                        rubSell = it.rubSell,
                        plnBuy = it.plnBuy,
                        plnSell = it.plnSell,
                        uahBuy = it.uahBuy,
                        uahSell = it.uahSell
                    )
                }
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun readBestCourses() {
        benchmarkRule.measureRepeated {
            runTest {
                persistenceDataSource.readBestCourses().first()
            }
        }
    }
}
