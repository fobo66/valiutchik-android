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

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.turbine.test
import com.google.common.truth.Correspondence
import com.google.common.truth.Truth.assertThat
import dev.fobo66.valiutchik.core.db.Bank
import dev.fobo66.valiutchik.core.db.Database
import dev.fobo66.valiutchik.core.db.LoadBestSellRates
import dev.fobo66.valiutchik.core.db.Rate
import fobo66.valiutchik.core.util.CURRENCY_NAME_EURO
import fobo66.valiutchik.core.util.CURRENCY_NAME_HRYVNIA
import fobo66.valiutchik.core.util.CURRENCY_NAME_US_DOLLAR
import java.util.concurrent.Executors
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

private const val RATE = 1.23456

class PersistenceDataSourceTest {
    private lateinit var persistenceDataSource: PersistenceDataSource
    private lateinit var db: Database

    private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @BeforeTest
    fun setUp() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Database.Schema.create(driver)
        db = Database(driver)
        persistenceDataSource = PersistenceDataSourceImpl(db, ioDispatcher)
        db.bankQueries.insertBank(Bank(id = 1, name = "bank", formattedName = "test")).await()
    }

    @AfterTest
    fun tearDown() {
        ioDispatcher.close()
    }

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
    fun `save rates`() = runTest {
        val rates =
            setOf(
                Rate(1, date, 1, CURRENCY_NAME_US_DOLLAR, RATE, RATE),
                Rate(
                    2,
                    date,
                    1,
                    CURRENCY_NAME_US_DOLLAR,
                    RATE,
                    RATE
                )
            )

        persistenceDataSource.saveRates(rates)

        val savedRates = db.rateQueries.loadAllRates().executeAsList()
        assertThat(savedRates).hasSize(2)
    }

    @Test
    fun `load best rates`() = runTest {
        val rates =
            setOf(
                Rate(1, date, 1, CURRENCY_NAME_US_DOLLAR, RATE, RATE),
                Rate(2, date, 1, CURRENCY_NAME_US_DOLLAR, RATE / 2, RATE),
                Rate(3, date, 1, CURRENCY_NAME_EURO, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        persistenceDataSource.readBestBuyCourses()
            .test {
                assertThat(awaitItem()).hasSize(2)
            }
    }

    @Test
    fun `missing currency`() = runTest {
        val rates =
            setOf(
                Rate(1, date, 1, CURRENCY_NAME_US_DOLLAR, RATE, RATE),
                Rate(2, date, 1, CURRENCY_NAME_EURO, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        persistenceDataSource.readBestSellCourses()
            .test {
                assertThat(
                    awaitItem()
                ).comparingElementsUsing(
                    Correspondence.transforming(
                        LoadBestSellRates::currencyId,
                        "currency id"
                    )
                )
                    .doesNotContain(CURRENCY_NAME_HRYVNIA)
            }
    }

    @Test
    fun `load old rates`() = runTest {
        val rates =
            setOf(
                Rate(1, date, 1, CURRENCY_NAME_US_DOLLAR, RATE, RATE),
                Rate(2, oldDate, 1, CURRENCY_NAME_EURO, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        val result = db.rateQueries.loadOldRates().executeAsList()
        assertThat(result).hasSize(1)
    }

    @Test
    fun `delete rates`() = runTest {
        val rates =
            setOf(
                Rate(1, date, 1, CURRENCY_NAME_US_DOLLAR, RATE, RATE),
                Rate(2, oldDate, 1, CURRENCY_NAME_EURO, RATE, RATE),
                Rate(3, oldDate, 1, CURRENCY_NAME_EURO, RATE, RATE)
            )

        persistenceDataSource.saveRates(rates)

        val ratesToDelete = db.rateQueries.loadAllRates().executeAsList()
            .filter { it.date == oldDate }

        persistenceDataSource.deleteRates(ratesToDelete)
        val savedRates = db.rateQueries.loadAllRates().executeAsList()
        assertThat(savedRates).hasSize(1)
    }
}
