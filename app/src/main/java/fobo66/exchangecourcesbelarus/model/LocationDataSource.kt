package fobo66.exchangecourcesbelarus.model

import android.location.Location
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import fobo66.exchangecourcesbelarus.util.Constants
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
class LocationDataSource {

  suspend fun resolveUserCity(location: Location): GeocodingResponse {

    val geocodingRequest = MapboxGeocoding.builder()
      .accessToken(Constants.GEOCODER_ACCESS_TOKEN)
      .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
      .query(
        Point.fromLngLat(
          location.getLongitude(),
          location.getLatitude()
        )
      )
      .languages("ru-RU")
      .country("by")
      .build()

    return geocodingRequest.await()
  }
}

private suspend fun MapboxGeocoding.await(): GeocodingResponse =
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
                "Response from Mapbox was null but response body type " +
                    "was declared as non-null"
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
