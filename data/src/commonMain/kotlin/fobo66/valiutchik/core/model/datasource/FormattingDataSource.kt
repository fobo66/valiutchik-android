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

internal const val LANG_BELARUSIAN = "bel"
internal const val LANG_RU = "rus"
internal const val BYN = "BYN"
internal const val CYRILLIC_LATIN = "Cyrillic-Latin"
internal const val BELARUSIAN_TRANSLITERATOR_ID = "Any_be-Cyrillic"
internal const val BELARUSIAN_RULES =
    "сск>ск;ло>ла;ре>рэ;ри>ры;ий>і;ый>ы;те>тэ;ше>шэ;Те>Тэ;Це>Цэ;и>і"

interface FormattingDataSource {
    /**
     * Format currency rate as a monetary value
     */
    fun formatCurrencyValue(value: Float): String

    /**
     * Clean up all the unnecessary parts from the bank name
     */
    fun formatBankName(name: String): String
}
