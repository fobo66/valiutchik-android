package fobo66.exchangecourcesbelarus.model.datasource

import com.mapbox.geojson.Point
import com.mapbox.search.Country
import com.mapbox.search.Language
import com.mapbox.search.QueryType.PLACE
import com.mapbox.search.ReverseGeoOptions
import com.mapbox.search.ReverseGeocodingSearchEngine
import com.mapbox.search.result.SearchResult
import fobo66.exchangecourcesbelarus.util.search
import fobo66.valiutchik.core.entities.Location
import javax.inject.Inject

class GeocodingDataSourceImpl @Inject constructor(
  private val geocodingSearchEngine: ReverseGeocodingSearchEngine
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
