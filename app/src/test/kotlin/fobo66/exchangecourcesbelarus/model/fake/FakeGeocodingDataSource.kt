package fobo66.exchangecourcesbelarus.model.fake

import com.mapbox.search.result.SearchAddress
import com.mapbox.search.result.SearchResult
import fobo66.exchangecourcesbelarus.model.datasource.GeocodingDataSource
import fobo66.valiutchik.core.entities.Location
import io.mockk.every
import io.mockk.mockk
import java.io.IOException

class FakeGeocodingDataSource : GeocodingDataSource, Resettable {
  var showError = false
  var unexpectedError = false

  private val searchAddress: SearchAddress = mockk {
    every {
      locality
    } returns "fake"
  }

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

  override fun reset() {
    showError = false
    unexpectedError = false
  }
}
