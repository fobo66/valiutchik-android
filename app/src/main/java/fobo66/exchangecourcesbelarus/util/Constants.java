package fobo66.exchangecourcesbelarus.util;

/**
 * Constants used in project
 *
 * Created by fobo66 on 09.08.2015.
 */
public final class Constants {
  public static final String GEOCODER_ACCESS_TOKEN =
      "pk.eyJ1IjoiZm9ibzY2IiwiYSI6ImNqZHJvZ2k1OTBsZjMzM3BkNG0zanoyMGMifQ.g825XQ7bhGHwSW0cggrJcQ";

  public static final String TEMPLATE_URI = "https://admin.myfin.by/outer/authXml/%d";

  public static final String USD = "USD";
  public static final String EUR = "EUR";
  public static final String RUR = "RUR";

  public static final boolean BUY_COURSE = true;
  public static final boolean SELL_COURSE = false;

  public static final int LOCATION_PERMISSION_REQUEST = 746;
  public static final int INTERNET_PERMISSIONS_REQUEST = 66;

  public static final String BROADCAST_ACTION_SUCCESS = "fobo66.exchangecourcesbelarus.CurrencyRateService.SUCCESS";
  public static final String BROADCAST_ACTION_ERROR = "fobo66.exchangecourcesbelarus.CurrencyRateService.ERROR";
  public static final String ACTION_FETCH_COURSES =
      "fobo66.exchangecourcesbelarus.action.FETCH_COURSES";
  public static final String EXTRA_CITY = "fobo66.exchangecourcesbelarus.extra.CITY";
  public static final String EXTRA_BESTCOURSES = "fobo66.exchangecourcesbelarus.extra.BESTCOURSES";
  public static final String EXTRA_BUYORSELL = "fobo66.exchangecourcesbelarus.extra.BUYORSELL";

  public static final String TIMESTAMP = "timestamp";

  public static final String FIREBASE_REGISTERING_KEY = "firebase_registering";
}
