/*
 *    Copyright 2022 Andrey Mukamolov
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

import com.mapbox.search.ResponseInfo
import com.mapbox.search.ReverseGeoOptions
import com.mapbox.search.SearchCallback
import com.mapbox.search.SearchEngine
import com.mapbox.search.result.SearchResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

internal suspend fun SearchEngine.search(
  options: ReverseGeoOptions
): List<SearchResult> =
  suspendCancellableCoroutine { continuation ->
    val task = search(
      options,
      object : SearchCallback {
        override fun onError(e: Exception) {
          if (continuation.isCancelled) {
            return
          }
          continuation.resumeWithException(e)
        }

        override fun onResults(results: List<SearchResult>, responseInfo: ResponseInfo) {
          continuation.resume(results)
        }
      }
    )

    continuation.invokeOnCancellation {
      task.cancel()
    }
  }
