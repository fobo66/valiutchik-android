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

package fobo66.valiutchik.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import dev.fobo66.valiutchik.core.db.Database
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.FileSystem
import org.koin.dsl.module
import org.w3c.dom.Worker

@OptIn(ExperimentalWasmJsInterop::class)
internal fun jsWorker(): Worker = js(
    """new Worker(new URL("./sqlite.worker.js", import.meta.url))"""
)

@OptIn(DelicateCoroutinesApi::class)
actual val thirdPartyModule = module {
    single<SqlDriver> {
        WebWorkerDriver(jsWorker()).also {
            GlobalScope.launch {
                Database.Schema.awaitCreate(it)
            }
        }
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve(PREFERENCES_NAME)
        }
    }
}
