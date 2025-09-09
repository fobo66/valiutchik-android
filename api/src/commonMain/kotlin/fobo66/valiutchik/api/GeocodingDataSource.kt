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

package fobo66.valiutchik.api

import fobo66.valiutchik.api.entity.Feature

/**
 * Datasource for geocoding. Supports only reverse geocoding at the moment
 */
interface GeocodingDataSource {

    /**
     * Find possible city address on given location
     *
     * @param latitude Latitude for search
     * @param longitude Longitude for search
     * @param ipAddress Optional IP address, if resolved
     */
    suspend fun findPlace(
        latitude: Double,
        longitude: Double,
        ipAddress: String? = null
    ): List<Feature>
}
