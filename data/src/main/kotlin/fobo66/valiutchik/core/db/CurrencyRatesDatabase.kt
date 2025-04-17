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
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.util.CurrencyName

@Database(
  entities = [BestCourse::class],
  version = 3,
  autoMigrations = [
    AutoMigration(from = 1, to = 2),
    AutoMigration(from = 2, to = 3),
  ],
)
abstract class CurrencyRatesDatabase : RoomDatabase() {
  abstract fun currencyRatesDao(): CurrencyRatesDao
  abstract fun currencyDao(): CurrencyDao
}

val MIGRATION_3_4 =
  object : Migration(3, 4) {
    private val CREATE_CURRENCIES_TABLE =
      "CREATE TABLE IF NOT EXISTS currency(id INT PRIMARY KEY, name TEXT NOT NULL);"

    override fun migrate(connection: SQLiteConnection) {
      connection.execSQL(CREATE_CURRENCIES_TABLE)
      val currencyFillQuery = buildString {
        append("INSERT INTO currency(name) VALUES ")
        repeat(CurrencyName.entries.size) {
          append("(?), ")
        }
        replace(length - 2, length - 1, ";")
      }
      val currencyFillStatement = connection.prepare(currencyFillQuery)

      CurrencyName.entries.forEachIndexed { index, name ->
        currencyFillStatement.bindText(index, name.name)
      }
      currencyFillStatement.step()
    }
  }
