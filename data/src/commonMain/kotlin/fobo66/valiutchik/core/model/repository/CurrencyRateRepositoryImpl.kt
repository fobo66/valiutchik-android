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

package fobo66.valiutchik.core.model.repository

import androidx.collection.ScatterMap
import androidx.collection.mutableScatterMapOf
import fobo66.valiutchik.api.CurrencyRatesDataSource
import fobo66.valiutchik.api.entity.UNDEFINED_BUY_RATE
import fobo66.valiutchik.api.entity.UNDEFINED_SELL_RATE
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.entities.LanguageTag
import fobo66.valiutchik.core.entities.toRate
import fobo66.valiutchik.core.model.datasource.FormattingDataSource
import fobo66.valiutchik.core.model.datasource.LocaleDataSource
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.util.CurrencyName.RUB
import fobo66.valiutchik.core.util.CurrencyName.UAH
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.io.IOException

private const val EXCHANGE_RATE_NORMALIZER = 100
private const val DEFAULT_CITY_INDEX = "1"

class CurrencyRateRepositoryImpl(
    private val persistenceDataSource: PersistenceDataSource,
    private val currencyRatesDataSource: CurrencyRatesDataSource,
    private val formattingDataSource: FormattingDataSource,
    private val localeDataSource: LocaleDataSource
) : CurrencyRateRepository {
    private val citiesMap: ScatterMap<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        mutableScatterMapOf(
            "Ashmyany" to "81",
            "Asipovichy" to "40",
            "Astravyets" to "82",
            "Babruysk" to "21",
            "Baranavichy" to "20",
            "Barysaw" to "22",
            "Biaroza" to "45",
            "Braslau" to "63",
            "Brest" to "5",
            "Bykhaw" to "64",
            "Chavusy" to "68",
            "Dobrush" to "69",
            "Drahichyn" to "75",
            "Dzyarzhynsk" to "37",
            "Hantsavichy" to "76",
            "Hlybokaye" to "59",
            "Homyel" to "3",
            "Horki" to "39",
            "Hrodna" to "4",
            "Ivanava" to "74",
            "Ivatsevichy" to "47",
            "Kalinkavichy" to "42",
            "Kastsiukovichy" to "66",
            "Khoiniki" to "71",
            "Kobryn" to "44",
            "Krychaw" to "41",
            "Lepel" to "60",
            "Lida" to "23",
            "Luniniec" to "46",
            "Mahilyow" to "6",
            "Maladzyechna" to "30",
            "Malaryta" to "78",
            "Maryina Horka" to "38",
            "Masty" to "79",
            "Mazyr" to "24",
            "Minsk" to "1",
            "Mstsislau" to "67",
            "Navahrudak" to "51",
            "Navalukoml" to "61",
            "Navapolatsk" to "25",
            "Nyasvizh" to "57",
            "Orsha" to "26",
            "Pastavy" to "58",
            "Pinsk" to "27",
            "Polatsk" to "28",
            "Pruzhany" to "73",
            "Pyetrykaw" to "72",
            "Rahachow" to "43",
            "Rechytsa" to "33",
            "Salihorsk" to "29",
            "Shchuchyn" to "80",
            "Shklov" to "65",
            "Slonim" to "48",
            "Sluck" to "34",
            "Smaliavichy" to "55",
            "Smarhon" to "50",
            "Stolin" to "77",
            "Stowbtsy" to "54",
            "Svietlahorsk" to "31",
            "Swislatsch" to "201",
            "Talochyn" to "62",
            "Vawkavysk" to "49",
            "Vileyka" to "36",
            "Vitsebsk" to "2",
            "Zaslawye" to "56",
            "Zhlobin" to "32",
            "Zhodzina" to "35",
            "Zhytkavichy" to "70"
        )
    }

    override suspend fun refreshExchangeRates(city: String, defaultCity: String) {
        val cityIndex = citiesMap[city] ?: citiesMap[defaultCity] ?: DEFAULT_CITY_INDEX
        val currencies =
            try {
                currencyRatesDataSource.loadExchangeRates(cityIndex)
            } catch (e: IOException) {
                throw CurrencyRatesLoadFailedException(e)
            }

        val rates = currencies.values.asFlow()
            .filter { it.isNotEmpty() }
            .map {
                it.toRate()
            }.toList()

        persistenceDataSource.saveRates(
            rates
        )
    }

    override fun formatBankName(rate: BestCourse, languageTag: LanguageTag): String =
        formattingDataSource.formatBankName(rate.bankName.orEmpty(), languageTag)

    override fun loadExchangeRates(): Flow<List<BestCourse>> =
        persistenceDataSource.readBestCourses()
            .map { courses ->
                courses
                    .filter {
                        it.bankName != null &&
                            it.currencyValue != null &&
                            it.currencyValue != UNDEFINED_BUY_RATE &&
                            it.currencyValue != UNDEFINED_SELL_RATE
                    }
            }

    override fun formatRate(rate: BestCourse, languageTag: LanguageTag): String =
        formattingDataSource.formatCurrencyValue(
            when (rate.currencyName) {
                RUB, UAH -> rate.currencyValue?.times(EXCHANGE_RATE_NORMALIZER) ?: 0.0f
                else -> rate.currencyValue ?: 0.0f
            },
            languageTag
        )

    override fun loadLocale(): Flow<LanguageTag> = localeDataSource.locale.distinctUntilChanged()
}
