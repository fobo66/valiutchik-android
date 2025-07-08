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

package fobo66.valiutchik.api.entity

/**
 * Currency model from XML
 */
data class Bank(
    val bankId: Long = 0L,
    val filialId: Long = 0L,
    val date: String = "",
    val bankName: String = "",
    val usdBuy: Double = UNDEFINED_BUY_RATE,
    val usdSell: Double = UNDEFINED_SELL_RATE,
    val eurBuy: Double = UNDEFINED_BUY_RATE,
    val eurSell: Double = UNDEFINED_SELL_RATE,
    val rubBuy: Double = UNDEFINED_BUY_RATE,
    val rubSell: Double = UNDEFINED_SELL_RATE,
    val plnBuy: Double = UNDEFINED_BUY_RATE,
    val plnSell: Double = UNDEFINED_SELL_RATE,
    val uahBuy: Double = UNDEFINED_BUY_RATE,
    val uahSell: Double = UNDEFINED_SELL_RATE
)
