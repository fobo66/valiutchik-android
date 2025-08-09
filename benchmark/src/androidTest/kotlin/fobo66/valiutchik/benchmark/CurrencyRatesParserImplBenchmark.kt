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
import androidx.test.platform.app.InstrumentationRegistry
import fobo66.valiutchik.api.CurrencyRatesParserCommonImpl
import fobo66.valiutchik.api.CurrencyRatesParserImpl
import fobo66.valiutchik.api.CurrencyRatesResponseParserImpl
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Benchmark for the XML parser
 */
@RunWith(AndroidJUnit4::class)
class CurrencyRatesParserImplBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val androidParser = CurrencyRatesParserImpl()
    private val commonParser = CurrencyRatesParserCommonImpl()
    private val jsonParser = CurrencyRatesResponseParserImpl(
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    )

    @Test
    fun customParser() {
        benchmarkRule.measureRepeated {
            val body = runWithMeasurementDisabled {
                loadResponseBody("myfinFeed.xml")
            }
            androidParser.parse(body)
        }
    }

    @Test
    fun commonParser() {
        benchmarkRule.measureRepeated {
            val body = runWithMeasurementDisabled {
                loadResponseBody("myfinFeed.xml")
            }
            commonParser.parse(body)
        }
    }

    @Test
    fun jsonParser() {
        benchmarkRule.measureRepeated {
            val body = runWithMeasurementDisabled {
                loadResponseBody("myfinNewApi.json")
            }
            jsonParser.parse(body)
        }
    }

    private fun loadResponseBody(fileName: String): String = InstrumentationRegistry
        .getInstrumentation()
        .context.assets
        .open(fileName)
        .bufferedReader()
        .readText()
}
