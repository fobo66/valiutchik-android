package fobo66.exchangecourcesbelarus.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * (c) 2017 Andrey Mukamolow <fobo66@protonmail.com>
 * Created 10/19/17.
 */

public class Util {

  public boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = connectManager.getActiveNetworkInfo();

    return netInfo != null && netInfo.isConnected();
  }
}
