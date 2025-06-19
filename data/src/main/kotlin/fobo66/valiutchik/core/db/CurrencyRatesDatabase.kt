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

package fobo66.valiutchik.core.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import fobo66.valiutchik.core.entities.Rate

@Database(
    entities = [Rate::class],
    version = 6,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = DeleteBestRatesTableMigration::class),
        AutoMigration(from = 5, to = 6)
    ]
)
abstract class CurrencyRatesDatabase : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
}

@DeleteTable.Entries(
    DeleteTable(
        tableName = "best_rates"
    )
)
class DeleteBestRatesTableMigration : AutoMigrationSpec
