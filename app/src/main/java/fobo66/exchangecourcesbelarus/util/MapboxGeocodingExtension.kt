package fobo66.exchangecourcesbelarus.util

import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.search.ResponseInfo
import com.mapbox.search.ReverseGeoOptions
import com.mapbox.search.ReverseGeocodingSearchEngine
import com.mapbox.search.SearchCallback
import com.mapbox.search.result.SearchResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

internal suspend fun MapboxGeocoding.await(): GeocodingResponse =
  suspendCancellableCoroutine { continuation ->
    enqueueCall(object : Callback<GeocodingResponse> {
      override fun onResponse(
        call: Call<GeocodingResponse>,
        response: Response<GeocodingResponse>
      ) {
        if (response.isSuccessful) {
          val body = response.body()
          if (body == null) {
            val e =
              KotlinNullPointerException(
                "Response from Mapbox was null but response body type was declared as non-null"
              )
            continuation.resumeWithException(e)
          } else {
            continuation.resume(body)
          }
        } else {
          continuation.resumeWithException(HttpException(response))
        }
      }

      override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
        // Don't bother with resuming the continuation if it is already cancelled.
        if (continuation.isCancelled) return
        continuation.resumeWithException(t)
      }
    })

    continuation.invokeOnCancellation {
      cancelCall()
    }
  }

internal suspend fun ReverseGeocodingSearchEngine.search(
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
