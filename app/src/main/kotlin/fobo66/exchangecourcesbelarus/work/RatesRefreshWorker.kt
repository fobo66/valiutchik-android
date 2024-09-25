/*
 *    Copyright 2024 Andrey Mukamolov
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

package fobo66.exchangecourcesbelarus.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRates
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRatesForDefaultCity
import kotlinx.datetime.Clock

const val WORKER_ARG_LOCATION_AVAILABLE = "isLocationAvailable"

class RatesRefreshWorker(
  private val refreshExchangeRates: ForceRefreshExchangeRates,
  private val refreshExchangeRatesForDefaultCity: ForceRefreshExchangeRatesForDefaultCity,
  appContext: Context,
  params: WorkerParameters
) :
  CoroutineWorker(appContext, params) {
  override suspend fun doWork(): Result {
    val isLocationAvailable = this.inputData.getBoolean(WORKER_ARG_LOCATION_AVAILABLE, false)

    if (isLocationAvailable) {
      refreshExchangeRates.execute(Clock.System.now())
    } else {
      refreshExchangeRatesForDefaultCity.execute(Clock.System.now())
    }
    return Result.success()
  }
}
