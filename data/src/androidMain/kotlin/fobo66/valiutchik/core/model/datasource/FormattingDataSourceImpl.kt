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
import android.icu.text.PluralRules
import android.icu.text.Transliterator
import android.icu.util.Currency
import android.icu.util.ULocale
import fobo66.valiutchik.core.entities.LanguageTag
import kotlin.LazyThreadSafetyMode.NONE

class FormattingDataSourceImpl : FormattingDataSource {
    private lateinit var cachedLocale: ULocale
    private var cachedLanguageTag: LanguageTag? = null

    private val targetCurrency: Currency by lazy(NONE) {
        Currency.getInstance(BYN)
    }

    override fun formatBankName(name: String, languageTag: LanguageTag): String {
        if (name.isEmpty()) {
            return name
        }

        checkLocaleCache(languageTag)

        val languageCode = ULocale.forLanguageTag(languageTag).isO3Language

        return if (languageCode == LANG_RU) {
            name
        } else {
            transliterate(
                name,
                languageCode
            )
        }
    }

    override fun formatCurrencyValue(value: Double, languageTag: LanguageTag): String {
        checkLocaleCache(languageTag)

        return NumberFormatter
            .withLocale(cachedLocale)
            .unit(targetCurrency)
            .format(value)
            .toString()
    }

    override fun formatCurrencyName(
        currencyCode: String,
        quantity: Long,
        languageTag: LanguageTag
    ): String {
        checkLocaleCache(languageTag)

        val pluralCount = PluralRules.forLocale(cachedLocale).select(quantity.toDouble())

        return Currency.getInstance(currencyCode)
            .getName(cachedLocale, Currency.PLURAL_LONG_NAME, pluralCount, null)
    }

    override fun formatCurrencySymbol(
        currencyCode: String,
        languageTag: LanguageTag
    ): String {
        checkLocaleCache(languageTag)

        return Currency.getInstance(currencyCode).getSymbol(cachedLocale)
    }

    private fun checkLocaleCache(languageTag: LanguageTag) {
        if (cachedLanguageTag != languageTag) {
            cachedLocale = ULocale.forLanguageTag(languageTag)
            cachedLanguageTag = languageTag
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
