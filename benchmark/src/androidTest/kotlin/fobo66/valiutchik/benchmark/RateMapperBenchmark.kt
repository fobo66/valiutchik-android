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

package fobo66.valiutchik.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.ExchangeRateValue
import fobo66.valiutchik.core.entities.toRate
import kotlinx.datetime.LocalDate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val ID = "1"
private const val NAME = "test"
private val RATE = ExchangeRateValue(1.23)
private val UNKNOWN_COURSE = ExchangeRateValue(-1.0)
private const val DATE = "2025-06-16"

@RunWith(AndroidJUnit4::class)
class RateMapperBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val fullBank = Bank(
        bankId = ID,
        filialId = ID,
        date = DATE,
        bankName = NAME,
        usdBuy = RATE,
        usdSell = RATE,
        eurBuy = RATE,
        eurSell = RATE,
        rubBuy = RATE,
        rubSell = RATE,
        plnBuy = RATE,
        plnSell = RATE,
        uahBuy = RATE,
        uahSell = RATE
    )
    private val partialBank = Bank(
        bankId = ID,
        filialId = ID,
        date = DATE,
        bankName = NAME,
        usdBuy = RATE,
        usdSell = RATE,
        eurBuy = RATE,
        eurSell = RATE,
        rubBuy = UNKNOWN_COURSE,
        rubSell = UNKNOWN_COURSE,
        plnBuy = RATE,
        plnSell = UNKNOWN_COURSE,
        uahBuy = RATE,
        uahSell = UNKNOWN_COURSE
    )

    private val emptyBank = Bank()

    @Test
    fun parseFullBank() = benchmarkRule.measureRepeated {
        fullBank.toRate(LocalDate.Formats.ISO, NAME)
    }

    @Test
    fun parseEmptyBank() = benchmarkRule.measureRepeated {
        emptyBank.toRate(LocalDate.Formats.ISO, NAME)
    }

    @Test
    fun parsePartialBank() = benchmarkRule.measureRepeated {
        partialBank.toRate(LocalDate.Formats.ISO, NAME)
    }
}
