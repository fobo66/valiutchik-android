package fobo66.exchangecourcesbelarus

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * (c) 2017 Andrey Mukamolov
 * Created 9/16/17.
 */
@HiltAndroidApp
open class App : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
}
