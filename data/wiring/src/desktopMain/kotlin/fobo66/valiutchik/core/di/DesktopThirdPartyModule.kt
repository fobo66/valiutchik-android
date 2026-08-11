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
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.fobo66.valiutchik.core.db.Database
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import java.io.File
import java.util.Properties
import okio.Path.Companion.toPath

@DependencyGraph(AppScope::class)
interface DesktopThirdPartyModule : ThirdPartyModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabase(): Database {
        val dbFile = File(System.getProperty("java.io.tmpdir"), DATABASE_NAME)
        val dbUrl = "jdbc:sqlite:${dbFile.absolutePath}"
        return Database(JdbcSqliteDriver(dbUrl, Properties(), Database.Schema.synchronous()))
    }

    @Provides
    @SingleIn(AppScope::class)
    fun providePreferences(): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath {
        val file = File(System.getProperty("java.io.tmpdir"), PREFERENCES_NAME)
        file.absolutePath.toPath()
    }
}
