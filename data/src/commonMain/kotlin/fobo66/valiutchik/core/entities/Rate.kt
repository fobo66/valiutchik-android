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
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rates",
    indices = [
        Index(
            "usdBuy",
            "usdSell",
            "eurBuy",
            "eurSell",
            "rubBuy",
            "rubSell",
            "plnBuy",
            "plnSell",
            "uahBuy",
            "uahSell"
        )
    ]
)
data class Rate(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: String,
    val bankName: String? = "",
    val usdBuy: Float = 0.0f,
    val usdSell: Float = 0.0f,
    val eurBuy: Float = 0.0f,
    val eurSell: Float = 0.0f,
    val rubBuy: Float = 0.0f,
    val rubSell: Float = 0.0f,
    val plnBuy: Float = 0.0f,
    val plnSell: Float = 0.0f,
    val uahBuy: Float = 0.0f,
    val uahSell: Float = 0.0f
)
