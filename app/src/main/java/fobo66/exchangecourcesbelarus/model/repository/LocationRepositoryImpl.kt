package fobo66.exchangecourcesbelarus.model.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.KEY_DEFAULT_CITY
import fobo66.valiutchik.core.USER_CITY_KEY
import fobo66.valiutchik.core.model.repository.LocationRepository
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
  private val locationDataSource: LocationDataSource,
  private val preferencesDataSource: PreferencesDataSource
) : LocationRepository {

  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveUserCity(): String {
    val response = try {
      Timber.v("Resolving user's location")
      val location = locationDataSource.resolveLocation()
      Timber.v("Resolved user's location: %s", location)
      locationDataSource.resolveUserCity(location)
    } catch (e: HttpException) {
      Timber.e(e, "Failed to determine user city")
      null
    }

    return if (response?.features()?.isNotEmpty() == true) {
      Timber.v("Resolving user's city")
      response.features()[0].text()?.also {
        Timber.v("Resolved user's city: %s", it)
        preferencesDataSource.saveString(USER_CITY_KEY, it)
      } ?: loadDefaultCity()
    } else {
      loadDefaultCity()
    }
  }

  private fun loadDefaultCity() = preferencesDataSource.loadString(KEY_DEFAULT_CITY, "Минск")
}
