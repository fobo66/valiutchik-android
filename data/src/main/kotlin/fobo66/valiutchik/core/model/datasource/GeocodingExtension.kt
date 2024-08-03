/*
 *    Copyright 2024 Andrey Mukamolov
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

import com.tomtom.sdk.search.common.error.SearchFailure
import com.tomtom.sdk.search.reversegeocoder.ReverseGeocoder
import com.tomtom.sdk.search.reversegeocoder.ReverseGeocoderCallback
import com.tomtom.sdk.search.reversegeocoder.ReverseGeocoderOptions
import com.tomtom.sdk.search.reversegeocoder.ReverseGeocoderResponse
import com.tomtom.sdk.search.reversegeocoder.model.location.PlaceMatch
import fobo66.valiutchik.api.entity.GeocodingFailedException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

internal suspend fun ReverseGeocoder.search(
  options: ReverseGeocoderOptions
): List<PlaceMatch> =
  suspendCancellableCoroutine { continuation ->
    val task = reverseGeocode(
      options,
      object : ReverseGeocoderCallback {
        override fun onFailure(failure: SearchFailure) {
          if (continuation.isCancelled) {
            return
          }
          continuation.resumeWithException(GeocodingFailedException(failure.message))
        }

        override fun onSuccess(result: ReverseGeocoderResponse) {
          continuation.resume(result.places)
        }
      }
    )

    continuation.invokeOnCancellation {
      task.cancel()
    }
  }
