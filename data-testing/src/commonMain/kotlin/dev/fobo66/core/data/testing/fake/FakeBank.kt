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

import fobo66.valiutchik.api.CURRENCY_ALIAS_EURO
import fobo66.valiutchik.api.CURRENCY_ALIAS_HRYVNIA
import fobo66.valiutchik.api.CURRENCY_ALIAS_RUBLE
import fobo66.valiutchik.api.CURRENCY_ALIAS_US_DOLLAR
import fobo66.valiutchik.api.CURRENCY_ALIAS_ZLOTY
import fobo66.valiutchik.api.entity.Currency
import fobo66.valiutchik.api.entity.CurrencyRateSource

const val ID = 1L
const val TEST = "test"
const val RATE = 1.23f
const val DATE = 1754725825L
const val PROCESSED_DATE = "2025-08-09T07:50:25Z"

@Suppress("LongParameterList") // ok for tests
fun buildBank(
    bankId: Long = ID,
    branchId: Long = ID,
    date: Long = DATE,
    bankName: String = TEST,
    usdBuy: Float = RATE,
    usdSell: Float = RATE,
    eurBuy: Float = RATE,
    eurSell: Float = RATE,
    rubBuy: Float = RATE,
    rubSell: Float = RATE,
    plnBuy: Float = RATE,
    plnSell: Float = RATE,
    uahBuy: Float = RATE,
    uahSell: Float = RATE
): List<CurrencyRateSource> = listOf(
    CurrencyRateSource(
        id = branchId,
        bankId = bankId,
        bankName = bankName,
        currency = Currency(
            buy = usdBuy,
            dateUpdate = date,
            name = CURRENCY_ALIAS_US_DOLLAR,
            sell = usdSell
        )
    ),
    CurrencyRateSource(
        bankId = bankId,
        bankName = bankName,
        currency = Currency(
            buy = eurBuy,
            dateUpdate = date,
            name = CURRENCY_ALIAS_EURO,
            sell = eurSell
        ),
        id = branchId
    ),
    CurrencyRateSource(
        id = branchId,
        bankId = bankId,
        bankName = bankName,
        currency = Currency(
            buy = plnBuy,
            dateUpdate = date,
            name = CURRENCY_ALIAS_ZLOTY,
            sell = plnSell
        )
    ),
    CurrencyRateSource(
        id = branchId,
        bankId = bankId,
        bankName = bankName,
        currency = Currency(
            buy = uahBuy,
            dateUpdate = date,
            name = CURRENCY_ALIAS_HRYVNIA,
            sell = uahSell
        )
    ),
    CurrencyRateSource(
        id = branchId,
        bankId = bankId,
        bankName = bankName,
        currency = Currency(
            buy = rubBuy,
            dateUpdate = date,
            name = CURRENCY_ALIAS_RUBLE,
            sell = rubSell
        )
    )
)
