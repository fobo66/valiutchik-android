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
    val usdBuy: Float = UNDEFINED_BUY_RATE,
    val usdSell: Float = UNDEFINED_SELL_RATE,
    val eurBuy: Float = UNDEFINED_BUY_RATE,
    val eurSell: Float = UNDEFINED_SELL_RATE,
    val rubBuy: Float = UNDEFINED_BUY_RATE,
    val rubSell: Float = UNDEFINED_SELL_RATE,
    val plnBuy: Float = UNDEFINED_BUY_RATE,
    val plnSell: Float = UNDEFINED_SELL_RATE,
    val uahBuy: Float = UNDEFINED_BUY_RATE,
    val uahSell: Float = UNDEFINED_SELL_RATE
)
