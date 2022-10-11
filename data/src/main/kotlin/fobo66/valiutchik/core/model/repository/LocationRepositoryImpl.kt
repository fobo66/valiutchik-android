package fobo66.valiutchik.core.model.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import fobo66.valiutchik.core.model.datasource.GeocodingDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import java.io.IOException
import javax.inject.Inject
import timber.log.Timber

class LocationRepositoryImpl @Inject constructor(
  private val locationDataSource: LocationDataSource,
  private val geocodingDataSource: GeocodingDataSource
) : LocationRepository {

  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  override suspend fun resolveUserCity(defaultCity: String): String {
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
        }
      } ?: defaultCity
  }
}