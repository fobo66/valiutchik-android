package fobo66.valiutchik.core.model.datasource

import com.mapbox.search.result.SearchResult
import fobo66.valiutchik.core.entities.Location

interface GeocodingDataSource {
  suspend fun resolveUserCity(location: Location): List<SearchResult>
}
