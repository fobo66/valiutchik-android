package fobo66.valiutchik.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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

  private val parser = fobo66.valiutchik.api.CurrencyRatesParserImpl()

  @Test
  fun parseMyfinFeed() {
    benchmarkRule.measureRepeated {
      val myfinFeedFileStream =
        InstrumentationRegistry.getInstrumentation().context.assets.open("myfinFeed.xml")
      parser.parse(myfinFeedFileStream)
    }
  }
}
