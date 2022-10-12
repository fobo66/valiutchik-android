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

package fobo66.valiutchik.core.model.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import fobo66.valiutchik.core.model.datasource.GeocodingDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import java.io.IOException
import javax.inject.Inject
import timber.log.Timber

class LocationRepositoryImpl @Inject constructor(
  private val locationDataSource: LocationDataSource,
  private val geocodingDataSource: GeocodingDataSource
) : LocationRepository {

  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveUserCity(defaultCity: String): String {
    val response = try {
      Timber.v("Resolving user's location")
      val location = locationDataSource.resolveLocation()
      Timber.v("Resolved user's location: %s", location)
      geocodingDataSource.resolveUserCity(location)
    } catch (e: IOException) {
      Timber.e(e, "Failed to determine user city")
      emptyList()
    }

    Timber.v("Resolving user's city")
    return response
      .asSequence()
      .map { it.address }
      .filterNotNull()
      .map { it.locality }
      .firstNotNullOfOrNull { it }.also { city ->
        city?.let {
          Timber.v("Resolved user's city: %s", it)
        }
      } ?: defaultCity
  }
}
