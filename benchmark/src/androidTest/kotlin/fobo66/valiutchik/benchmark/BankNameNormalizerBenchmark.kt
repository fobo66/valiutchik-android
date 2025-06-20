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
import fobo66.valiutchik.core.util.BankNameNormalizer
import fobo66.valiutchik.core.util.BankNameNormalizerImpl
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val BANK_NAME = "ЗАО «Статусбанк» (бывш. \"Евроторгинвестбанк\")"

/**
 * Benchmark for cleaning up bank name from API
 */
@RunWith(AndroidJUnit4::class)
class BankNameNormalizerBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val codeNormalizer: BankNameNormalizer = BankNameNormalizerImpl()

    @Test
    fun normalizeViaCode() = benchmarkRule.measureRepeated {
        codeNormalizer.normalize(BANK_NAME)
    }
}
