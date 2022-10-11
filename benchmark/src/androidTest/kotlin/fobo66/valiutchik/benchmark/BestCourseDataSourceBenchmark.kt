package fobo66.valiutchik.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fobo66.valiutchik.core.model.datasource.BestCourseDataSourceImpl
import org.junit.Before
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

  private var currencies: Set<fobo66.valiutchik.api.Currency> = setOf()

  @Before
  fun setUp() {
    val myfinFeedFileStream =
      InstrumentationRegistry.getInstrumentation().context.assets.open("myfinFeed.xml")
    currencies = parser.parse(myfinFeedFileStream)
  }

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