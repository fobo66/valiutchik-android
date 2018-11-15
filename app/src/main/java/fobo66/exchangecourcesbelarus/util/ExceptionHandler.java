package fobo66.exchangecourcesbelarus.util;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import fobo66.exchangecourcesbelarus.BuildConfig;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 17.03.2017.
 */

public class ExceptionHandler {
    public static void handleException(Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e("ExchangeCourses", "Exception raised: ", e);
        }
        Crashlytics.logException(e);
    }
}
