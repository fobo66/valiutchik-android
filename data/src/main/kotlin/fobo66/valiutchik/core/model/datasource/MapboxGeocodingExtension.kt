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