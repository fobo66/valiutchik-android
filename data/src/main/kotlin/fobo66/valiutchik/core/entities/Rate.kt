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

package fobo66.valiutchik.core.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates")
data class Rate(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: String,
    val bankName: String? = "",
    val usdBuy: Double = 0.0,
    val usdSell: Double = 0.0,
    val eurBuy: Double = 0.0,
    val eurSell: Double = 0.0,
    val rubBuy: Double = 0.0,
    val rubSell: Double = 0.0,
    val plnBuy: Double = 0.0,
    val plnSell: Double = 0.0,
    val uahBuy: Double = 0.0,
    val uahSell: Double = 0.0
)
