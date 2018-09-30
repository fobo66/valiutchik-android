package fobo66.exchangecourcesbelarus;

import androidx.multidex.MultiDexApplication;
import com.google.firebase.database.FirebaseDatabase;

/**
 * (c) 2017 Andrey Mukamolow <fobo66@protonmail.com>
 * Created 9/16/17.
 */

public class App extends MultiDexApplication {
  @Override public void onCreate() {
    super.onCreate();
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
  }
}
