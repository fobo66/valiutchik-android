package fobo66.exchangecourcesbelarus.model.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import fobo66.exchangecourcesbelarus.model.datasource.GeocodingDataSource
import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.KEY_DEFAULT_CITY
import fobo66.valiutchik.core.USER_CITY_KEY
import fobo66.valiutchik.core.model.repository.LocationRepository
import java.io.IOException
import javax.inject.Inject
import timber.log.Timber

class LocationRepositoryImpl @Inject constructor(
  private val locationDataSource: LocationDataSource,
  private val geocodingDataSource: GeocodingDataSource,
  private val preferencesDataSource: PreferencesDataSource
) : LocationRepository {

  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveUserCity(): String {
    val response = try {
      Timber.v("Resolving user's location")
      val location = locationDataSource.resolveLocation()
      Timber.v("Resolved user's location: %s", location)
      geocodingDataSource.resolveUserCity(location)
    } catch (e: IOException) {
      Timber.e(e, "Failed to determine user city")
      emptyList()
    }

    Timber.v("Resolving user's city")
    return response
      .asSequence()
      .map { it.address }
      .filterNotNull()
      .map { it.locality }
      .firstNotNullOfOrNull { it }.also { city ->
        city?.let {
          Timber.v("Resolved user's city: %s", it)
          preferencesDataSource.saveString(USER_CITY_KEY, it)
        }
      } ?: loadDefaultCity()
  }

  private suspend fun loadDefaultCity() = preferencesDataSource.loadString(KEY_DEFAULT_CITY, "Минск")
}
