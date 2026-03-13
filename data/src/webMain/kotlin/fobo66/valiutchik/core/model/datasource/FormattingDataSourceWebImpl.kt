/*
 *    Copyright 2026 Andrey Mukamolov
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

import androidx.collection.ScatterMap
import androidx.collection.mutableScatterMapOf
import doist.x.normalize.Form
import doist.x.normalize.normalize
import fobo66.valiutchik.core.entities.LanguageTag

@OptIn(ExperimentalWasmJsInterop::class)
private fun formatCurrency(value: Double, language: String): String = js(
    "new Intl.NumberFormat(language, {currency: 'BYN', style: 'currency'}).format(value)"
)

@OptIn(ExperimentalWasmJsInterop::class)
private fun resolveCurrencyName(value: String, language: String): String = js(
    "new Intl.DisplayNames([language], {type: 'currency', style: 'narrow'}).of(value)"
)

class FormattingDataSourceWebImpl : FormattingDataSource {

    private val cyrillicToLatinFirstAssociations: ScatterMap<String, String> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        mutableScatterMapOf(
            "а" to "a",
            "б" to "b",
            "в" to "v",
            "г" to "g",
            "д" to "d",
            "з" to "z",
            "и" to "i",
            "й" to "y",
            "к" to "k",
            "л" to "l",
            "м" to "m",
            "н" to "n",
            "о" to "o",
            "п" to "p",
            "р" to "r",
            "с" to "s",
            "т" to "t",
            "у" to "u",
            "ф" to "f",
            "ь" to "",
            "ъ" to "",
            "ы" to "i",
            "э" to "e",
            "е" to "ye",
            "ё" to "yo",
            "ж" to "zh",
            "х" to "kh",
            "ц" to "ts",
            "ч" to "ch",
            "ш" to "sh",
            "щ" to "shch",
            "ю" to "yu",
            "я" to "ya"
        )
    }

    private val cyrillicToLatinNonFirstAssociations: ScatterMap<String, String> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        mutableScatterMapOf(
            "а" to "a",
            "б" to "b",
            "в" to "v",
            "д" to "d",
            "з" to "z",
            "и" to "i",
            "й" to "i",
            "к" to "k",
            "л" to "l",
            "м" to "m",
            "н" to "n",
            "о" to "o",
            "п" to "p",
            "р" to "r",
            "с" to "s",
            "т" to "t",
            "у" to "u",
            "ф" to "f",
            "ь" to "",
            "е" to "e",
            "ё" to "yo",
            "ж" to "zh",
            "х" to "kh",
            "ц" to "ts",
            "ч" to "ch",
            "ш" to "sh",
            "щ" to "shch",
            "ю" to "yu",
            "я" to "ya"
        )
    }

    override fun formatCurrencyValue(value: Double, languageTag: LanguageTag): String =
        formatCurrency(value, languageTag)

    override fun formatCurrencyName(
        currencyCode: String,
        quantity: Long,
        languageTag: LanguageTag
    ): String = currencyCode.uppercase()

    override fun formatCurrencySymbol(currencyCode: String, languageTag: LanguageTag): String =
        resolveCurrencyName(currencyCode, languageTag)

    override fun formatBankName(name: String, languageTag: LanguageTag): String =
        cyrillicToLatin(name)

    private fun cyrillicToLatin(input: String): String {
        if (input.isEmpty()) {
            return input
        }

        val normalizedInput = input.normalize(Form.NFC)

        val latinString = buildString {
            var isWordBoundary = false

            for ((i, currentChar) in normalizedInput.withIndex()) {
                val isUpperCaseOrWhatever = currentChar == currentChar.uppercaseChar()
                val currentCharLowercase = currentChar.lowercase()

                if (currentCharLowercase == " ") {
                    isWordBoundary = true
                    continue
                }

                var newLetter: String

                if (i == 0 || isWordBoundary) {
                    newLetter = cyrillicToLatinFirstAssociations[currentCharLowercase].orEmpty()
                    isWordBoundary = false
                } else {
                    newLetter = cyrillicToLatinNonFirstAssociations[currentCharLowercase].orEmpty()
                }

                if (newLetter.isEmpty()) {
                    append(
                        if (isUpperCaseOrWhatever) {
                            currentCharLowercase.uppercase()
                        } else {
                            currentCharLowercase
                        }
                    )
                } else if (isUpperCaseOrWhatever) {
                    // handle multi-symbol letters
                    append(
                        if (newLetter.length > 1) {
                            newLetter[0].uppercase() + newLetter.substring(1)
                        } else {
                            newLetter.uppercase()
                        }
                    )
                } else {
                    append(newLetter)
                }
            }
        }
        return latinString
    }
}
