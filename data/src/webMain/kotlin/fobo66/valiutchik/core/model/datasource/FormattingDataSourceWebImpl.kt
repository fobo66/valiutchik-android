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

import fobo66.valiutchik.core.entities.LanguageTag

@OptIn(ExperimentalWasmJsInterop::class)
private fun formatCurrency(value: Double, language: String): String = js(
    "Intl.NumberFormat(language, {currency: 'BYN', style: 'currency'}).format(value)"
)

class FormattingDataSourceWebImpl : FormattingDataSource {
    override fun formatCurrencyValue(value: Double, languageTag: LanguageTag): String {
        val formatted = formatCurrency(value, languageTag)
        return formatted
    }

    override fun formatCurrencyName(
        currencyCode: String,
        quantity: Long,
        languageTag: LanguageTag
    ): String {
        TODO("Not yet implemented")
    }

    override fun formatCurrencySymbol(currencyCode: String, languageTag: LanguageTag): String {
        TODO("Not yet implemented")
    }

    override fun formatBankName(name: String, languageTag: LanguageTag): String {
        TODO("Not yet implemented")
    }
}
