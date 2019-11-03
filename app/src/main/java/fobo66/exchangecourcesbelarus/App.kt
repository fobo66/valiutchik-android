package fobo66.exchangecourcesbelarus

import androidx.multidex.MultiDexApplication
import com.google.firebase.database.FirebaseDatabase

/**
 * (c) 2017 Andrey Mukamolov
 * Created 9/16/17.
 */
class App : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()
    FirebaseDatabase.getInstance().setPersistenceEnabled(true)
  }
}