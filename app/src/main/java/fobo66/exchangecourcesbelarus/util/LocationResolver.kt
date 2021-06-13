package fobo66.exchangecourcesbelarus.util

import android.Manifest
import android.app.Activity
import androidx.annotation.RequiresPermission

interface LocationResolver {
  @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
  suspend fun resolveLocation(activity: Activity): Pair<Double, Double>
}
