/*
 *    Copyright 2025 Andrey Mukamolov
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
import android.location.LocationManager
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import fobo66.valiutchik.core.entities.Location
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val LOCATION_FIX_TIME_DURATION_HOURS = 3

class LocationDataSourceImpl(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : LocationDataSource {

    private val noLocation by lazy(LazyThreadSafetyMode.NONE) {
        Location(
            latitude = UNKNOWN_COORDINATE,
            longitude = UNKNOWN_COORDINATE
        )
    }

    private val locationFixTimeMaximum: Long by lazy {
        LOCATION_FIX_TIME_DURATION_HOURS.hours.inWholeNanoseconds
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
                    .filter {
                        SystemClock.elapsedRealtimeNanos() - it.elapsedRealtimeNanos <=
                            locationFixTimeMaximum
                    }
                    .maxByOrNull { it.elapsedRealtimeNanos }
            }

            location?.let {
                Location(it.latitude, it.longitude)
            } ?: noLocation
        } else {
            noLocation
        }
    }
}
