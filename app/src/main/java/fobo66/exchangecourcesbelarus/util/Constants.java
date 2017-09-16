package fobo66.exchangecourcesbelarus.util;

/**
 * Constants used in project
 *
 * Created by fobo66 on 09.08.2015.
 */
public final class Constants {
  public static final String GEOCODER_API_KEY = "AIzaSyAFkmb5zd8r-eaPsbHw9lyCvExXOrGfNU0";

  public static final String MINSK_URI = "https://admin.myfin.by/outer/authXml/1";
  public static final String VITEBSK_URI = "https://admin.myfin.by/outer/authXml/2";
  public static final String GOMEL_URI = "https://admin.myfin.by/outer/authXml/3";
  public static final String GRODNO_URI = "https://admin.myfin.by/outer/authXml/4";
  public static final String BREST_URI = "https://admin.myfin.by/outer/authXml/5";
  public static final String MOGILEV_URI = "https://admin.myfin.by/outer/authXml/6";

  public static final String TEMPLATE_URI = "https://admin.myfin.by/outer/authXml/%d";

  public static final String USD = "USD";
  public static final String EUR = "EUR";
  public static final String RUR = "RUR";

  public static final boolean BUY_COURSE = true;
  public static final boolean SELL_COURSE = false;

  public static final int LOCATION_PERMISSION_REQUEST = 746;
  public static final int INTERNET_PERMISSIONS_REQUEST = 66;

  public static final String BROADCAST_ACTION = "fobo66.exchangecourcesbelarus.BROADCAST";
  public static final String ACTION_FETCH_COURSES =
      "fobo66.exchangecourcesbelarus.action.FETCH_COURSES";
  public static final String EXTRA_CITY = "fobo66.exchangecourcesbelarus.extra.CITY";
  public static final String EXTRA_BESTCOURSES = "fobo66.exchangecourcesbelarus.extra.BESTCOURSES";
  public static final String PREFERENCES = "fobo66.exchangecourcesbelarus.Preferences";
}
