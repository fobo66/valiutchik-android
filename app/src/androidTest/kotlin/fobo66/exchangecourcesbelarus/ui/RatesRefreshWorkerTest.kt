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

package fobo66.exchangecourcesbelarus.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.google.common.truth.Truth.assertThat
import dev.fobo66.domain.testing.fake.FakeForceRefreshExchangeRates
import dev.fobo66.domain.testing.fake.FakeForceRefreshExchangeRatesForDefaultCity
import fobo66.exchangecourcesbelarus.work.RatesRefreshWorker
import fobo66.exchangecourcesbelarus.work.WORKER_ARG_LOCATION_AVAILABLE
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RatesRefreshWorkerTest {
    private val forceRefreshExchangeRates = FakeForceRefreshExchangeRates()
    private val forceRefreshExchangeRatesForDefaultCity =
        FakeForceRefreshExchangeRatesForDefaultCity()

    @Test
    fun runWorkerWithNoPermission() {
        val worker = prepareWorker(isLocationAvailable = false)
        runTest {
            val result = worker.doWork()
            assertThat(result).isEqualTo(ListenableWorker.Result.success())
            assertThat(forceRefreshExchangeRates.isRefreshed).isFalse()
            assertThat(forceRefreshExchangeRatesForDefaultCity.isRefreshed).isTrue()
        }
    }

    @Test
    fun runWorkerWithPermission() {
        val worker = prepareWorker(isLocationAvailable = true)
        runTest {
            val result = worker.doWork()
            assertThat(result).isEqualTo(ListenableWorker.Result.success())
            assertThat(forceRefreshExchangeRates.isRefreshed).isTrue()
            assertThat(forceRefreshExchangeRatesForDefaultCity.isRefreshed).isFalse()
        }
    }

    private fun prepareWorker(isLocationAvailable: Boolean = false): RatesRefreshWorker =
        TestListenableWorkerBuilder<RatesRefreshWorker>(
            context = ApplicationProvider.getApplicationContext(),
            inputData = workDataOf(WORKER_ARG_LOCATION_AVAILABLE to isLocationAvailable)
        ).setWorkerFactory(
            object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker? = RatesRefreshWorker(
                    forceRefreshExchangeRates,
                    forceRefreshExchangeRatesForDefaultCity,
                    appContext,
                    workerParameters
                )
            }
        ).build()
}
