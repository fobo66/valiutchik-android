package fobo66.valiutchik.core.model.datasource

import android.Manifest
import androidx.annotation.RequiresPermission
import fobo66.valiutchik.core.entities.Location

interface LocationDataSource {
  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  suspend fun resolveLocation(): Location
}
