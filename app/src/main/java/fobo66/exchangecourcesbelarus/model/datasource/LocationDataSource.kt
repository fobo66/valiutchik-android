package fobo66.exchangecourcesbelarus.model.datasource

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import fobo66.exchangecourcesbelarus.di.GeocoderAccessToken
import fobo66.exchangecourcesbelarus.di.Io
import fobo66.exchangecourcesbelarus.util.LocationResolverOldSchoolImpl
import fobo66.valiutchik.core.entities.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Singleton
class LocationDataSource @Inject constructor(
  @ApplicationContext private val context: Context,
  @Io private val ioDispatcher: CoroutineDispatcher,
  @GeocoderAccessToken private val geocoderAccessToken: String
) {

  private val locationFixTimeMaximum: Long by lazy {
    Duration.ofHours(LocationResolverOldSchoolImpl.LOCATION_FIX_TIME_DURATION_HOURS).toNanos()
  }

  private val geocodingRequestTemplate: MapboxGeocoding.Builder by lazy {
    MapboxGeocoding.builder()
      .accessToken(geocoderAccessToken)
      .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
      .languages("ru-RU")
      .country("by")
  }

  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  suspend fun resolveLocation(): Location {
    val locationManager = context.getSystemService<LocationManager>() ?: return NO_LOCATION

    return if (LocationManagerCompat.isLocationEnabled(locationManager)) {
      var location: android.location.Location? = null
      var tempLocation: android.location.Location?
      var locationFixTime: Long

      withContext(ioDispatcher) {
        locationManager.getProviders(true).forEach {
          Timber.v("Trying provider %s", it)
          tempLocation = locationManager.getLastKnownLocation(it)
          Timber.v("Location from provider %s : %s", it, tempLocation)
          locationFixTime = tempLocation?.elapsedRealtimeNanos ?: 0
          if (tempLocation != null && SystemClock.elapsedRealtimeNanos() - locationFixTime <= locationFixTimeMaximum) {
            location = tempLocation
          }
        }
      }

      location?.let {
        Location(it.latitude, it.longitude)
      } ?: NO_LOCATION
    } else {
      NO_LOCATION
    }
  }

  suspend fun resolveUserCity(location: Location): GeocodingResponse {

    val geocodingRequest = geocodingRequestTemplate
      .query(
        Point.fromLngLat(
          location.longitude,
          location.latitude
        )
      )
      .build()

    return geocodingRequest.await()
  }

  companion object {
    private val NO_LOCATION = Location(0.0, 0.0)
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
