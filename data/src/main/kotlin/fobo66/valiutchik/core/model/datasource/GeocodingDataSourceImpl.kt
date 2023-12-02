/*
 *    Copyright 2023 Andrey Mukamolov
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

import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.search.reversegeocoder.ReverseGeocoder
import com.tomtom.sdk.search.reversegeocoder.ReverseGeocoderOptions
import com.tomtom.sdk.search.reversegeocoder.model.location.PlaceMatch
import fobo66.valiutchik.core.entities.Location
import javax.inject.Inject

class GeocodingDataSourceImpl @Inject constructor(
  private val geocoder: ReverseGeocoder
) : GeocodingDataSource {
  override suspend fun findPlace(location: Location): List<PlaceMatch> {
    val coordinate = GeoPoint(latitude = location.latitude, longitude = location.longitude)
    val options = ReverseGeocoderOptions(
      position = coordinate,
    )

    return geocoder.search(options)
  }
}
