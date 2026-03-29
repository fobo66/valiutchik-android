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

package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.LanguageTag
import fobo66.valiutchik.core.model.datasource.FormattingDataSource
import fobo66.valiutchik.core.model.datasource.LocaleDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class LocaleRepositoryImpl(
    private val formattingDataSource: FormattingDataSource,
    private val localeDataSource: LocaleDataSource
) : LocaleRepository {
    override fun formatBankName(rate: BestCourse, languageTag: LanguageTag): String =
        formattingDataSource.formatBankName(rate.bankName, languageTag)

    override fun formatCurrencyName(rate: BestCourse, languageTag: LanguageTag): String =
        formattingDataSource.formatCurrencyName(rate.currencyName, rate.multiplier, languageTag)

    override fun formatCurrencySymbol(rate: BestCourse, languageTag: LanguageTag): String =
        formattingDataSource.formatCurrencySymbol(rate.currencyName, languageTag)

    override fun formatRate(rate: BestCourse, languageTag: LanguageTag): String =
        formattingDataSource.formatCurrencyValue(
            rate.currencyValue * rate.multiplier,
            languageTag
        )

    override fun loadLocale(): Flow<LanguageTag> = localeDataSource.locale.distinctUntilChanged()
}
