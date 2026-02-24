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
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.fobo66.valiutchik.core.db.Database
import dev.fobo66.valiutchik.core.db.Rate
import fobo66.valiutchik.api.ApiResponseParser
import fobo66.valiutchik.api.ApiResponseParserImpl
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSourceImpl
import fobo66.valiutchik.core.util.CURRENCY_NAME_US_DOLLAR
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.Json
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

    private lateinit var db: Database

    private val parser: ApiResponseParser = ApiResponseParserImpl(
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    )

    private val persistenceDataSource: PersistenceDataSource = PersistenceDataSourceImpl(db)

    @Before
    fun setUp() = runTest {
        val driver =
            AndroidSqliteDriver(Database.Schema, ApplicationProvider.getApplicationContext())
        Database.Schema.create(driver)
        db = Database(driver)
        val ratesResponseContent =
            InstrumentationRegistry
                .getInstrumentation()
                .context.assets
                .open("myfinNewApi.json")
                .asSource()
                .buffered()

        persistenceDataSource.saveRates(
            parser.parseRates(ratesResponseContent)
                .map {
                    Rate(
                        id = 0L,
                        date = LocalDate.now().toString(),
                        bankId = it.bankId,
                        buyRate = it.currency.buy,
                        sellRate = it.currency.sell,
                        currencyId = it.currency.name
                    )
                }
        )
    }

    @Test
    fun readBestCourses() {
        benchmarkRule.measureRepeated {
            runTest {
                persistenceDataSource.readBestSellCourses(listOf(CURRENCY_NAME_US_DOLLAR)).first()
            }
        }
    }
}
