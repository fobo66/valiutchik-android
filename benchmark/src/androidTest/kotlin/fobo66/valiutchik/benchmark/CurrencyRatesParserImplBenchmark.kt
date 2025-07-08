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
import fobo66.valiutchik.api.CurrencyRatesParserSerializerImpl
import nl.adaptivity.xmlutil.serialization.XML
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
    private val kotlinxSerializationParser =
        CurrencyRatesParserSerializerImpl(
            XML {
                defaultPolicy { ignoreUnknownChildren() }
            }
        )

    @Test
    fun customParser() {
        benchmarkRule.measureRepeated {
            val body = runWithMeasurementDisabled {
                loadResponseBody()
            }
            androidParser.parse(body)
        }
    }

    @Test
    fun commonParser() {
        benchmarkRule.measureRepeated {
            val body = runWithMeasurementDisabled {
                loadResponseBody()
            }
            commonParser.parse(body)
        }
    }

    @Test
    fun kotlinxSerialization() {
        benchmarkRule.measureRepeated {
            val body = runWithMeasurementDisabled {
                loadResponseBody()
            }
            kotlinxSerializationParser.parse(body)
        }
    }

    private fun loadResponseBody(): String = InstrumentationRegistry
        .getInstrumentation()
        .context.assets
        .open("myfinFeed.xml")
        .bufferedReader()
        .readText()
}
