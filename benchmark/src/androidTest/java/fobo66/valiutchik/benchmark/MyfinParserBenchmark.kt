package fobo66.valiutchik.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fobo66.valiutchik.core.util.MyfinParser
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Benchmark, which will execute on an Android device.
 *
 * The body of [BenchmarkRule.measureRepeated] is measured in a loop, and Studio will
 * output the result. Modify your code to see how it affects performance.
 */
@RunWith(AndroidJUnit4::class)
class MyfinParserBenchmark {

  @get:Rule
  val benchmarkRule = BenchmarkRule()

  private val parser = MyfinParser()

  @Test
  fun parseMyfinFeed() {
    benchmarkRule.measureRepeated {
      val myfinFeedFileStream =
        InstrumentationRegistry.getInstrumentation().context.assets.open("myfinFeed.xml")
      parser.parse(myfinFeedFileStream)
    }
  }
}