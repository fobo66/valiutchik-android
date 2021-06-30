package fobo66.exchangecourcesbelarus.util

import android.Manifest
import android.app.Activity
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject

class LocationResolverOldSchoolImpl @Inject constructor() : LocationResolver {

  private val locationFixTimeMaximum: Long by lazy {
    Duration.ofHours(LOCATION_FIX_TIME_DURATION_HOURS).toNanos()
  }

  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveLocation(activity: Activity): Pair<Double, Double> {
    val noLocation = 0.0 to 0.0
    val locationManager = activity.getSystemService<LocationManager>() ?: return noLocation

    return if (LocationManagerCompat.isLocationEnabled(locationManager)) {
      var location: Location? = null
      var tempLocation: Location?
      var locationFixTime: Long

      locationManager.getProviders(true).forEach {
        Timber.v("Trying provider %s", it)
        tempLocation = locationManager.getLastKnownLocation(it)
        Timber.v("Location from provider %s : %s", it, tempLocation)
        locationFixTime = tempLocation?.elapsedRealtimeNanos ?: 0
        if (tempLocation != null && SystemClock.elapsedRealtimeNanos() - locationFixTime <= locationFixTimeMaximum) {
          location = tempLocation
        }
      }

      location?.let {
        it.latitude to it.longitude
      } ?: noLocation
    } else {
      noLocation
    }
  }

  companion object {
    const val LOCATION_FIX_TIME_DURATION_HOURS = 3L
  }
}
