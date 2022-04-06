package fobo66.exchangecourcesbelarus.model.datasource

import android.Manifest
import androidx.annotation.RequiresPermission
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import fobo66.valiutchik.core.entities.Location

interface LocationDataSource {
  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  suspend fun resolveLocation(): Location

  suspend fun resolveUserCity(location: Location): GeocodingResponse
}
