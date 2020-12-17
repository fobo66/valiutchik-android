package fobo66.exchangecourcesbelarus

import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import fobo66.exchangecourcesbelarus.util.CrashlyticsTree
import timber.log.Timber

/**
 * (c) 2017 Andrey Mukamolov
 * Created 9/16/17.
 */
@HiltAndroidApp
open class App : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(CrashlyticsTree(FirebaseCrashlytics.getInstance()))
    }
  }
}