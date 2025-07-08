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
import dev.fobo66.core.data.testing.fake.buildBank
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.UNDEFINED_BUY_RATE
import fobo66.valiutchik.api.entity.UNDEFINED_SELL_RATE
import fobo66.valiutchik.core.entities.toRate
import kotlinx.datetime.LocalDate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RateMapperBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val fullBank = buildBank()
    private val partialBank = buildBank(
        rubBuy = UNDEFINED_BUY_RATE,
        rubSell = UNDEFINED_SELL_RATE,
        plnSell = UNDEFINED_SELL_RATE,
        uahSell = UNDEFINED_SELL_RATE
    )

    private val emptyBank = Bank()

    @Test
    fun parseFullBank() = benchmarkRule.measureRepeated {
        fullBank.toRate(LocalDate.Formats.ISO)
    }

    @Test
    fun parseEmptyBank() = benchmarkRule.measureRepeated {
        emptyBank.toRate(LocalDate.Formats.ISO)
    }

    @Test
    fun parsePartialBank() = benchmarkRule.measureRepeated {
        partialBank.toRate(LocalDate.Formats.ISO)
    }
}
