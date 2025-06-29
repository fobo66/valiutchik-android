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

import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSource
import io.github.aakira.napier.Napier

const val URI_SCHEME = "geo"
const val URI_AUTHORITY = "0,0"
const val URI_PARAM_KEY = "q"

class MapRepositoryImpl(
    private val uriDataSource: UriDataSource,
    private val intentDataSource: IntentDataSource
) : MapRepository {
    override fun searchOnMap(query: CharSequence): String? {
        val mapUri =
            uriDataSource.prepareUri(
                URI_SCHEME,
                URI_AUTHORITY,
                URI_PARAM_KEY,
                query.toString()
            )
        val intent = intentDataSource.createIntentUri(mapUri, "TODO")

        val canResolveIntent = intentDataSource.checkIntentUri(intent)

        return if (canResolveIntent) {
            intent
        } else {
            Napier.e("Cannot show banks on map: maps app not found")
            null
        }
    }
}
