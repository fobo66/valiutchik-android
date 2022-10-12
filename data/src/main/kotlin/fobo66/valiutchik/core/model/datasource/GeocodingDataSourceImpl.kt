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

import com.mapbox.geojson.Point
import com.mapbox.search.Country
import com.mapbox.search.Language
import com.mapbox.search.QueryType.PLACE
import com.mapbox.search.ReverseGeoOptions
import com.mapbox.search.SearchEngine
import com.mapbox.search.result.SearchResult
import fobo66.valiutchik.core.entities.Location
import javax.inject.Inject

class GeocodingDataSourceImpl @Inject constructor(
  private val geocodingSearchEngine: SearchEngine
) : GeocodingDataSource {
  private val searchLanguages: List<Language> by lazy {
    listOf(Language("ru-RU"))
  }

  private val searchCountries: List<Country> by lazy {
    listOf(Country("by"))
  }

  override suspend fun resolveUserCity(location: Location): List<SearchResult> {
    val options = ReverseGeoOptions(
      center = Point.fromLngLat(location.longitude, location.latitude),
      types = listOf(PLACE),
      languages = searchLanguages,
      countries = searchCountries
    )

    return geocodingSearchEngine.search(options)
  }
}
