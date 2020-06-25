package fobo66.exchangecourcesbelarus

import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import fobo66.exchangecourcesbelarus.di.ApplicationComponent
import fobo66.exchangecourcesbelarus.di.DaggerApplicationComponent
import fobo66.exchangecourcesbelarus.di.DaggerComponentProvider
import fobo66.exchangecourcesbelarus.util.CrashlyticsTree
import timber.log.Timber

/**
 * (c) 2017 Andrey Mukamolov
 * Created 9/16/17.
 */
class App : MultiDexApplication(), DaggerComponentProvider {

  override val component: ApplicationComponent by lazy {
    // Creates an instance of AppComponent using its Factory constructor
    // We pass the applicationContext that will be used as Context in the graph
    DaggerApplicationComponent.factory().create(applicationContext)
  }

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(CrashlyticsTree(FirebaseCrashlytics.getInstance()))
    }
  }
}