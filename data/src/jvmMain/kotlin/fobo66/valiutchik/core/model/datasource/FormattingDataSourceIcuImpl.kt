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

import com.ibm.icu.number.NumberFormatter
import com.ibm.icu.text.Transliterator
import com.ibm.icu.util.Currency
import com.ibm.icu.util.ULocale
import fobo66.valiutchik.core.util.BankNameNormalizer
import kotlin.LazyThreadSafetyMode.NONE

class FormattingDataSourceIcuImpl(
    private val locale: ULocale,
    private val bankNameNormalizer: BankNameNormalizer
) : FormattingDataSource {
    private val targetCurrency: Currency by lazy(NONE) {
        Currency.getInstance(BYN)
    }

    /**
     * Format currency rate as a monetary value
     */
    override fun formatCurrencyValue(value: Double): String = NumberFormatter
        .withLocale(locale)
        .unit(targetCurrency)
        .format(value)
        .toString()

    /**
     * Clean up all the unnecessary parts from the bank name
     */
    override fun formatBankName(name: String): String {
        if (name.isEmpty()) {
            return name
        }
        val normalizedName = bankNameNormalizer.normalize(name)
        val languageCode = locale.isO3Language

        return if (languageCode == LANG_RU) {
            normalizedName
        } else {
            transliterate(
                normalizedName,
                languageCode
            )
        }
    }

    private fun transliterate(bankName: String, languageCode: String): String {
        val transliterator =
            if (languageCode == LANG_BELARUSIAN) {
                Transliterator.createFromRules(
                    BELARUSIAN_TRANSLITERATOR_ID,
                    BELARUSIAN_RULES,
                    Transliterator.FORWARD
                )
            } else {
                Transliterator.getInstance(CYRILLIC_LATIN)
            }
        return transliterator.transliterate(bankName)
    }
}
