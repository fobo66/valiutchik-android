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

package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.core.util.BankNameNormalizer
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

class FormattingDataSourceNSStringImpl(private val bankNameNormalizer: BankNameNormalizer) :
    FormattingDataSource {
    /**
     * Format currency rate as a monetary value
     */
    override fun formatCurrencyValue(value: Double): String {
        val numberFormatter = NSNumberFormatter().apply {
            currencyCode = "BYN"
            maximumFractionDigits = 2u
            minimumFractionDigits = 2u
        }

        return numberFormatter.stringFromNumber(NSNumber(value)).orEmpty()
    }

    /**
     * Clean up all the unnecessary parts from the bank name
     */
    override fun formatBankName(name: String): String = bankNameNormalizer.normalize(name)
}
