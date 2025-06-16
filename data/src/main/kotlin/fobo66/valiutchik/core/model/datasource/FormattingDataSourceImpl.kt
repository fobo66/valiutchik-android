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
import fobo66.valiutchik.core.util.BankNameNormalizer
import java.util.Locale
import kotlin.LazyThreadSafetyMode.NONE

internal const val LANG_BELARUSIAN = "bel"
internal const val LANG_RU = "rus"
private const val BYN = "BYN"
private const val CYRILLIC_LATIN = "Cyrillic-Latin"
private const val BELARUSIAN_TRANSLITERATOR_ID = "Any_be-Cyrillic"
private const val BELARUSIAN_RULES =
    "сск>ск;ло>ла;ре>рэ;ри>ры;ий>і;ый>ы;те>тэ;ше>шэ;Те>Тэ;Це>Цэ;и>і"

class FormattingDataSourceImpl(
    private val locale: Locale,
    private val bankNameNormalizer: BankNameNormalizer
) : FormattingDataSource {
    override fun formatBankName(name: String): String {
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

    private val targetCurrency: Currency by lazy(NONE) {
        Currency.getInstance(BYN)
    }

    override fun formatCurrencyValue(value: Double): String = NumberFormatter
        .withLocale(locale)
        .unit(targetCurrency)
        .format(value)
        .toString()

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
