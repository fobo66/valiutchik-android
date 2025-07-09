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

import com.eygraber.uri.Uri

internal const val URI_SCHEME = "geo"
internal const val URI_AUTHORITY = "0,0"
internal const val URI_PARAM_KEY = "q"

class UriDataSourceImpl : UriDataSource {
    override fun prepareUri(query: CharSequence): Uri = Uri.Builder()
        .scheme(URI_SCHEME)
        .authority(URI_AUTHORITY)
        .appendQueryParameter(URI_PARAM_KEY, query.toString())
        .build()
}
