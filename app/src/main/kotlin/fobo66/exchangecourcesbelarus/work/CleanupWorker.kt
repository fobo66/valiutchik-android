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

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fobo66.valiutchik.domain.usecases.CleanUpOldRates
import io.github.aakira.napier.Napier
import kotlin.time.measureTime

class CleanupWorker(
    private val cleanUpOldRates: CleanUpOldRates,
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val cleanupTime = measureTime {
            cleanUpOldRates.execute()
        }

        Napier.d {
            "Cleanup took ${cleanupTime.inWholeMilliseconds} ms"
        }
        return Result.success()
    }
}
