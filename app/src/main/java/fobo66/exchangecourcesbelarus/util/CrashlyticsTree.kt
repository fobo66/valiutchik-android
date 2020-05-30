package fobo66.exchangecourcesbelarus.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * Tree for reporting caught exceptions to Crashlytics
 */
class CrashlyticsTree(private val crashlytics: FirebaseCrashlytics) : Timber.Tree() {

  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    if (t != null) {
      crashlytics.recordException(t)
    }

    crashlytics.log(message)
  }
}