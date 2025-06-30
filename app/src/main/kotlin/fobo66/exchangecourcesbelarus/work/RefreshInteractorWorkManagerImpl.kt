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

package fobo66.exchangecourcesbelarus.work

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import fobo66.valiutchik.domain.usecases.RefreshInteractor
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val WORK_BACKGROUND_REFRESH = "backgroundRefresh"

class RefreshInteractorWorkManagerImpl(private val workManager: WorkManager) : RefreshInteractor {
    override val isRefreshInProgress: Flow<Boolean>
        get() = workManager.getWorkInfosForUniqueWorkFlow(WORK_BACKGROUND_REFRESH)
            .map { infos ->
                infos.any { info ->
                    info.state == WorkInfo.State.RUNNING
                }
            }

    override suspend fun handleRefresh(isLocationAvailable: Boolean, updateInterval: Long) {
        val workRequest =
            PeriodicWorkRequestBuilder<RatesRefreshWorker>(updateInterval, TimeUnit.HOURS)
                .setConstraints(
                    Constraints
                        .Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                        .build()
                ).setInputData(workDataOf(WORKER_ARG_LOCATION_AVAILABLE to isLocationAvailable))
                .build()
        workManager.enqueueUniquePeriodicWork(
            WORK_BACKGROUND_REFRESH,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }
}
