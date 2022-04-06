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
import fobo66.exchangecourcesbelarus.util.await
import fobo66.valiutchik.core.entities.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Singleton
class LocationDataSourceImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  @Io private val ioDispatcher: CoroutineDispatcher,
  @GeocoderAccessToken private val geocoderAccessToken: String
) : LocationDataSource {

  private val noLocation by lazy {
    Location(0.0, 0.0)
  }

  private val locationFixTimeMaximum: Long by lazy {
    Duration.ofHours(LOCATION_FIX_TIME_DURATION_HOURS).toNanos()
  }

  private val geocodingRequestTemplate: MapboxGeocoding.Builder by lazy {
    MapboxGeocoding.builder()
      .accessToken(geocoderAccessToken)
      .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
      .languages("ru-RU")
      .country("by")
  }

  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveLocation(): Location {
    val locationManager = context.getSystemService<LocationManager>() ?: return noLocation

    return if (LocationManagerCompat.isLocationEnabled(locationManager)) {
      var location: android.location.Location? = null

      withContext(ioDispatcher) {
        location = locationManager.getProviders(true)
          .asSequence()
          .map { locationManager.getLastKnownLocation(it) }
          .filterNotNull()
          .find {
            SystemClock.elapsedRealtimeNanos() - it.elapsedRealtimeNanos <= locationFixTimeMaximum
          }
      }

      location?.let {
        Location(it.latitude, it.longitude)
      } ?: noLocation
    } else {
      noLocation
    }
  }

  override suspend fun resolveUserCity(location: Location): GeocodingResponse {

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
    private const val LOCATION_FIX_TIME_DURATION_HOURS = 3L
  }
}
