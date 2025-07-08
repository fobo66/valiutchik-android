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

package dev.fobo66.core.data.testing.fake

import fobo66.valiutchik.api.entity.Bank

const val ID = 1L
const val TEST = "test"
const val RATE = 1.23
const val DATE = "2025-07-01"
const val RAW_DATE = "01.07.2025"

@Suppress("LongParameterList") // ok for tests
fun buildBank(
    bankId: Long = ID,
    filialId: Long = ID,
    date: String = DATE,
    bankName: String = TEST,
    usdBuy: Double = RATE,
    usdSell: Double = RATE,
    eurBuy: Double = RATE,
    eurSell: Double = RATE,
    rubBuy: Double = RATE,
    rubSell: Double = RATE,
    plnBuy: Double = RATE,
    plnSell: Double = RATE,
    uahBuy: Double = RATE,
    uahSell: Double = RATE
): Bank = Bank(
    bankId,
    filialId,
    date,
    bankName,
    usdBuy,
    usdSell,
    eurBuy,
    eurSell,
    rubBuy,
    rubSell,
    plnBuy,
    plnSell,
    uahBuy,
    uahSell
)
