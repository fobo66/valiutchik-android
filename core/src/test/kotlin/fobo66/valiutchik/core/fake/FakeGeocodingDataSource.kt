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

  override suspend fun resolveUserCity(location: Location): List<SearchResult> =
    when {
      showError -> throw IOException("Yikes!")
      unexpectedError -> throw KotlinNullPointerException("Yikes!")
      else -> listOf(searchResult)
    }
}
