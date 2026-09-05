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

import android.content.Context
import android.icu.util.Currency
import android.icu.util.ULocale
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.platformstorage.PlatformStorage
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.entities.LanguageTag
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.withContext

class SearchDataSourceImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : SearchDataSource {
    override suspend fun index(bestCourses: List<BestCourse>, languageTag: LanguageTag) =
        withContext(dispatcher) {
            val locale = ULocale.forLanguageTag(languageTag)
            val searchContext = PlatformStorage.SearchContext.Builder(context, "byn-exchange-rates")
                .build()
            val searchSession = PlatformStorage.createSearchSessionAsync(searchContext).await()
            Napier.d { "Initialized search session" }
            val setSchemaRequest =
                SetSchemaRequest.Builder().addDocumentClasses(BestSearchableRate::class.java)
                    .build()

            val setSchemaResult = searchSession.setSchemaAsync(setSchemaRequest).await()
            Napier.d { "Initialized search schema: ${setSchemaResult.migrationFailures}" }
            val putDocumentsRequest = PutDocumentsRequest.Builder()
                .addDocuments(
                    bestCourses.map {
                        BestSearchableRate(
                            id = "${it.currencyId}-${it.isBuy == true}",
                            namespace = "byn-rates",
                            rate = "${
                                resolveCurrencyName(
                                    locale,
                                    it.currencyName
                                )
                            } ${it.currencyValue * it.multiplier} ${
                                resolveCurrencyName(locale, "BYN")
                            }",
                            type = if (it.isBuy == true) {
                                "buyrate"
                            } else {
                                "sellrate"
                            }
                        )
                    }
                )
                .build()
            val putResult = searchSession.putAsync(putDocumentsRequest).await()
            Napier.d { "Put data to search: ${putResult.isSuccess}" }
        }

    private fun resolveCurrencyName(locale: ULocale, symbol: String): String? =
        Currency.getInstance(
            symbol
        ).getName(locale, Currency.LONG_NAME, null)
}
