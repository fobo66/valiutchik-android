package fobo66.exchangecourcesbelarus.util

import android.Manifest
import android.app.Activity
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import androidx.core.os.CancellationSignal
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationResolverOldSchoolImpl @Inject constructor() : LocationResolver {
  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveLocation(activity: Activity): Pair<Double, Double> =
    suspendCancellableCoroutine { continuation ->
      val noLocation = 0.0 to 0.0
      val locationManager = activity.getSystemService<LocationManager>()

      if (locationManager == null) {
        continuation.resume(noLocation)
      }

      if (LocationManagerCompat.isLocationEnabled(locationManager!!)) {
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
          if (!continuation.isCancelled) {
            continuation.cancel()
          }
        }

        LocationManagerCompat.getCurrentLocation(
          locationManager,
          LocationManager.NETWORK_PROVIDER,
          cancellationSignal,
          Executors.newSingleThreadExecutor(),
          { location ->
            continuation.resume(location?.let {
              it.latitude to it.longitude
            } ?: noLocation)
          }
        )
      } else {
        continuation.resume(noLocation)
      }
    }
}
