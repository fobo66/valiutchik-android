package fobo66.exchangecourcesbelarus.model.datasource

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import fobo66.exchangecourcesbelarus.di.Io
import fobo66.valiutchik.core.entities.Location
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Singleton
class LocationDataSourceImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  @Io private val ioDispatcher: CoroutineDispatcher
) : LocationDataSource {

  private val noLocation by lazy {
    Location(0.0, 0.0)
  }

  private val locationFixTimeMaximum: Long by lazy {
    Duration.ofHours(LOCATION_FIX_TIME_DURATION_HOURS).toNanos()
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

  companion object {
    private const val LOCATION_FIX_TIME_DURATION_HOURS = 3L
  }
}
