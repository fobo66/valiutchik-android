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

package fobo66.valiutchik.core.fake

import com.mapbox.search.result.SearchAddress
import com.mapbox.search.result.SearchResult
import fobo66.valiutchik.core.entities.Location
import fobo66.valiutchik.core.model.datasource.GeocodingDataSource
import io.mockk.every
import io.mockk.mockk
import java.io.IOException

class FakeGeocodingDataSource : GeocodingDataSource {
  var showError = false
  var unexpectedError = false

  private val searchAddress: SearchAddress = SearchAddress(locality = "fake")

  private val searchResult: SearchResult = mockk {
    every {
      address
    } returns searchAddress
  }

  override suspend fun findPlace(location: Location): List<SearchResult> =
    when {
      showError -> throw IOException("Yikes!")
      unexpectedError -> throw KotlinNullPointerException("Yikes!")
      else -> listOf(searchResult)
    }
}
