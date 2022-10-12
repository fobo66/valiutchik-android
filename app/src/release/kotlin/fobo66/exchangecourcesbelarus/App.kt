package fobo66.exchangecourcesbelarus

import androidx.multidex.MultiDexApplication
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.search.MapboxSearchSdk
import dagger.hilt.android.HiltAndroidApp

/**
 * (c) 2017 Andrey Mukamolov
 * Created 9/16/17.
 */
@HiltAndroidApp
open class App : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()
  }
}
