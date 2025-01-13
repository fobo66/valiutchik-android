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
import fobo66.valiutchik.core.model.datasource.BestCourseDataSourceImpl
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Benchmark for the algorithm for resolving best course
 */
@RunWith(AndroidJUnit4::class)
class BestCourseDataSourceBenchmark {
  @get:Rule
  val benchmarkRule = BenchmarkRule()

  private val parser = fobo66.valiutchik.api.CurrencyRatesParserImpl()

  private val bestCourseDataSource = BestCourseDataSourceImpl()

  private val myfinFeedFileStream =
    InstrumentationRegistry
      .getInstrumentation()
      .context.assets
      .open("myfinFeed.xml")
  private val currencies = parser.parse(myfinFeedFileStream)

  @Test
  fun findBestBuyCurrencies() {
    benchmarkRule.measureRepeated {
      bestCourseDataSource.findBestBuyCurrencies(currencies)
    }
  }

  @Test
  fun findBestSellCurrencies() {
    benchmarkRule.measureRepeated {
      bestCourseDataSource.findBestSellCurrencies(currencies)
    }
  }
}
