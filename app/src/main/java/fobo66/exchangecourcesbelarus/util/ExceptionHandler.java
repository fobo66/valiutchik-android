package fobo66.exchangecourcesbelarus.util;

import com.crashlytics.android.Crashlytics;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 17.03.2017.
 */

public class ExceptionHandler {
    public static void handleException(Exception e) {
        e.printStackTrace();
        Crashlytics.logException(e);
    }
}
