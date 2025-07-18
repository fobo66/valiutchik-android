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

package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.api.GeocodingDataSource
import fobo66.valiutchik.api.entity.GeocodingFailedException
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import io.github.aakira.napier.Napier

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource,
    private val geocodingDataSource: GeocodingDataSource
) : LocationRepository {

    override suspend fun resolveUserCity(defaultCity: String): String {
        val response = try {
            Napier.v("Resolving user's location")
            val location = locationDataSource.resolveLocation()
            Napier.v { "Resolved user's location: $location" }
            geocodingDataSource.findPlace(location.latitude, location.longitude)
        } catch (e: GeocodingFailedException) {
            Napier.e("Failed to determine user city", e)
            emptyList()
        }

        Napier.v("Resolving user's city")
        return response
            .map { it.properties.city }
            .firstNotNullOfOrNull { it }.also { city ->
                city?.let {
                    Napier.v { "Resolved user's city: $it" }
                }
            } ?: defaultCity
    }
}
