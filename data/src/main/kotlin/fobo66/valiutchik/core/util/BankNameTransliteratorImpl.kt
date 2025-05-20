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

package fobo66.valiutchik.core.util

import android.icu.text.Transliterator
import android.os.Build
import io.github.aakira.napier.Napier

private const val LANG_BELARUSIAN = "bel"

class BankNameTransliteratorImpl : BankNameTransliterator {
    /**
     * Transliterate bank name if necessary
     */
    override fun transliterate(bankName: String, languageCode: String): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val transliteratorIds = Transliterator.getAvailableIDs()
            while (transliteratorIds.hasMoreElements()) {
                Napier.d {
                    "Transliterator id: ${transliteratorIds.nextElement()}"
                }
            }
            bankName
        } else {
            bankName
        }
}
