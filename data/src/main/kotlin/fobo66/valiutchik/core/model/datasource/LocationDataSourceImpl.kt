/*
 *    Copyright 2022 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.core.model.datasource

import android.Manifest
import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import fobo66.valiutchik.core.di.Io
import fobo66.valiutchik.core.entities.Location
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

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
        val criteria = Criteria().apply {
          accuracy = Criteria.ACCURACY_COARSE
        }
        location = locationManager.getProviders(criteria, true)
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
