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
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import app.cash.sqldelight.db.SqlDriver
import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDatabaseType
import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDriver
import dev.fobo66.valiutchik.core.db.Database
import kotlinx.coroutines.DelicateCoroutinesApi
import okio.FileSystem
import org.koin.dsl.module
import org.w3c.dom.MODULE
import org.w3c.dom.Worker
import org.w3c.dom.WorkerOptions
import org.w3c.dom.WorkerType

@OptIn(ExperimentalWasmJsInterop::class)
internal fun jsWorker(): Worker = Worker(jsWorkerUrl(), WorkerOptions(type = WorkerType.MODULE))

@OptIn(ExperimentalWasmJsInterop::class)
internal fun jsWorkerUrl(): String = js(
    """new URL("./sqlite.worker.js", import.meta.url).toString()"""
)

@OptIn(DelicateCoroutinesApi::class)
actual val thirdPartyModule = module {
    single<SqlDriver> {
        AndroidxSqliteDriver(
            driver = WebWorkerSQLiteDriver(jsWorker()),
            databaseType = AndroidxSqliteDatabaseType.File(DATABASE_NAME),
            schema = Database.Schema
        )
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve(PREFERENCES_NAME)
        }
    }
}
