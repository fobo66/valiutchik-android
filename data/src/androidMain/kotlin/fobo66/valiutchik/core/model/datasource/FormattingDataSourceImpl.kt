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

import android.icu.number.NumberFormatter
import android.icu.text.Transliterator
import android.icu.util.Currency
import android.icu.util.ULocale
import fobo66.valiutchik.core.entities.LanguageTag
import fobo66.valiutchik.core.util.BankNameNormalizer
import kotlin.LazyThreadSafetyMode.NONE

class FormattingDataSourceImpl(private val bankNameNormalizer: BankNameNormalizer) :
    FormattingDataSource {
    private lateinit var cachedLocale: ULocale
    private var cachedLanguageTag: LanguageTag? = null

    override fun formatBankName(name: String, languageTag: LanguageTag): String {
        if (name.isEmpty()) {
            return name
        }

        if (cachedLanguageTag != languageTag) {
            cachedLocale = ULocale.forLanguageTag(languageTag)
            cachedLanguageTag = languageTag
        }

        val normalizedName = bankNameNormalizer.normalize(name)
        val languageCode = ULocale.forLanguageTag(languageTag).isO3Language

        return if (languageCode == LANG_RU) {
            normalizedName
        } else {
            transliterate(
                normalizedName,
                languageCode
            )
        }
    }

    private val targetCurrency: Currency by lazy(NONE) {
        Currency.getInstance(BYN)
    }

    override fun formatCurrencyValue(value: Float, languageTag: LanguageTag): String {
        if (cachedLanguageTag != languageTag) {
            cachedLocale = ULocale.forLanguageTag(languageTag)
            cachedLanguageTag = languageTag
        }

        return NumberFormatter
            .withLocale(cachedLocale)
            .unit(targetCurrency)
            .format(value)
            .toString()
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
