package fobo66.exchangecourcesbelarus.util

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Integrate OkHttp code with coroutines.
 *
 * Taken from [kotlin-coroutines-okhttp project](https://github.com/gildor/kotlin-coroutines-okhttp)
 * with modifications
 */
suspend fun Call.await(): Response {
  return suspendCancellableCoroutine { continuation ->
    enqueue(object : Callback {
      override fun onResponse(call: Call, response: Response) {
        continuation.resume(response)
      }

      override fun onFailure(call: Call, e: IOException) {
        // Don't bother with resuming the continuation if it is already cancelled.
        if (continuation.isCancelled) return
        continuation.resumeWithException(e)
      }
    })

    continuation.invokeOnCancellation {
      cancel()
    }
  }
}
