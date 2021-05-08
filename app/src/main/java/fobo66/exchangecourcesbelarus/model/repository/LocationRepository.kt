package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.USER_CITY_KEY
import fobo66.valiutchik.core.entities.Location
import javax.inject.Inject

class LocationRepository @Inject constructor(
  private val locationDataSource: LocationDataSource,
  private val preferencesDataSource: PreferencesDataSource
) {

  suspend fun resolveUserCity(latitude: Double, longitude: Double): String {
    val response = try {
      locationDataSource.resolveUserCity(Location(latitude, longitude))
    } catch (e: Exception) {
      null
    }

    return if (response?.features()?.isNotEmpty() == true) {
      response.features()[0].text()?.also {
        preferencesDataSource.saveString(USER_CITY_KEY, it)
      } ?: loadDefaultCity()
    } else {
      loadDefaultCity()
    }
  }

  private fun loadDefaultCity() = preferencesDataSource.loadSting("default_city", "Минск")
}
