package fobo66.exchangecourcesbelarus.util

import android.Manifest
import android.app.Activity
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationResolverImpl @Inject constructor() : LocationResolver {
  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveLocation(activity: Activity): Pair<Double, Double> {
    val locationProviderClient =
      LocationServices.getFusedLocationProviderClient(activity)

    val location: Location? = locationProviderClient.lastLocation.await()

    return location?.let {
      it.latitude to it.longitude
    } ?: 0.0 to 0.0
  }
}
