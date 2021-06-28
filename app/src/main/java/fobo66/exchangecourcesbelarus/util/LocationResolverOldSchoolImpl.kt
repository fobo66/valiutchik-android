package fobo66.exchangecourcesbelarus.util

import android.Manifest
import android.app.Activity
import android.location.Location
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import javax.inject.Inject

class LocationResolverOldSchoolImpl @Inject constructor() : LocationResolver {
  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveLocation(activity: Activity): Pair<Double, Double> {
    val noLocation = 0.0 to 0.0
    val locationManager = activity.getSystemService<LocationManager>() ?: return noLocation

    return if (LocationManagerCompat.isLocationEnabled(locationManager)) {
      val location: Location? =
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

      location?.let {
        it.latitude to it.longitude
      } ?: noLocation
    } else {
      noLocation
    }
  }
}
