package fobo66.exchangecourcesbelarus.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import fobo66.exchangecourcesbelarus.R;
import fobo66.exchangecourcesbelarus.entities.BestCourse;
import fobo66.exchangecourcesbelarus.entities.Currency;
import fobo66.exchangecourcesbelarus.util.CertificateManager;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.xmlpull.v1.XmlPullParserException;

import static org.apache.commons.io.IOUtil.copy;

public class CurrencyRateService extends JobIntentService {

  private static final int JOB_ID = 228;
  private static final long MAX_STALE_PERIOD = 10800000; // 3 hours in ms

  private SharedPreferences prefs;

  private OkHttpClient client;

  private final Map<String, Integer> citiesMap = new HashMap<>();
  private boolean buyOrSell;

  public CurrencyRateService() {
    super();
    setupCitiesMap();
  }

  @Override public void onCreate() {
    super.onCreate();

    CertificateManager certificateManager = new CertificateManager();
    try {
      certificateManager.createTrustManagerForCertificate(
          getResources().openRawResource(R.raw.myfinby));
    } catch (GeneralSecurityException e) {
      throw new RuntimeException("Cannot load myfin certificate", e);
    }

    // 5 MiB
    long httpCacheSize = 1024 * 1024 * 5;

    client = new OkHttpClient.Builder().cache(new Cache(getCacheDir(), httpCacheSize))
        .sslSocketFactory(certificateManager.getTrustedSocketFactory(),
            certificateManager.getTrustManager())
        .build();

    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
  }

  @Override protected void onHandleWork(@NonNull Intent intent) {
    final String action = intent.getAction();
    if (Constants.ACTION_FETCH_COURSES.equals(action)) {
      final String city = intent.getStringExtra(Constants.EXTRA_CITY);
      buyOrSell = intent.getBooleanExtra(Constants.EXTRA_BUYORSELL, false);
      resolveBestCurrencyRates(city);
    }
  }

  public static void fetchCourses(Context context, String city, boolean buyOrSell) {
    Intent intent = new Intent(context, CurrencyRateService.class);
    intent.setAction(Constants.ACTION_FETCH_COURSES);
    intent.putExtra(Constants.EXTRA_CITY, city);
    intent.putExtra(Constants.EXTRA_BUYORSELL, buyOrSell);
    enqueueWork(context, CurrencyRateService.class, JOB_ID, intent);
  }

  /**
   * Handle action in the provided background thread with the provided
   * parameters.
   */
  private void resolveBestCurrencyRates(String city) {
    String url;
    long timeStamp;
    long currentTime = System.currentTimeMillis();
    if (city != null) {
      timeStamp = prefs.getLong(Constants.TIMESTAMP, currentTime - MAX_STALE_PERIOD);
      if ((currentTime - timeStamp) >= MAX_STALE_PERIOD) {
        url = getUrlForCity(city);
        Request request = prepareRequest(url);
        client.newCall(request).enqueue(new Callback() {
          @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
            ExceptionHandler.handleException(e);
            sendError();
          }

          @Override public void onResponse(@NonNull Call call, @NonNull Response response)
              throws IOException {
            if (response.isSuccessful()) {
              Reader responseCharStream = response.body().charStream();
              File xmlCache = new File(getCacheDir(), getString(R.string.feed_xml_name));
              xmlCache.createNewFile();
              OutputStream xmlStream = new FileOutputStream(xmlCache);
              try {
                copy(responseCharStream, xmlStream);
                saveTimestamp();
                readCached();
              } finally {
                xmlStream.close();
                responseCharStream.close();
              }
            }
          }
        });
      }
      try {
        readCached();
      } catch (Exception e) {
        ExceptionHandler.handleException(e);
        sendError();
      }
    }
  }

  private Request prepareRequest(String url) {
    String credential = Credentials.basic("app", "android");
    return new Request.Builder().url(url)
        .addHeader("Authorization", credential)
        .cacheControl(new CacheControl.Builder().maxAge(3, TimeUnit.HOURS).build())
        .build();
  }

  @NonNull private String getUrlForCity(String city) {
    Integer cityIndex = citiesMap.get(city);
    if (cityIndex == null) {
      cityIndex = 1;
    }

    return String.format(Locale.getDefault(), Constants.TEMPLATE_URI, cityIndex);
  }

  private void saveTimestamp() {
    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong(Constants.TIMESTAMP, System.currentTimeMillis());
    editor.apply();
  }

  private void readCached() {
    File cache = new File(getCacheDir(), getString(R.string.feed_xml_name));
    if (cache.exists() && cache.length() > 0) {
      try (InputStream cachedStream = new FileInputStream(cache)) {
        CurrencyEvaluator currencyEvaluator = new CurrencyEvaluator();
        CurrencyCourseParser parser = new MyfinParser();
        List<Currency> entries = parser.parse(cachedStream);
        Set<Currency> currencyTempSet = new HashSet<>(entries);
        final List<BestCourse> best =
            (buyOrSell) ? (currencyEvaluator.findBestBuyCourses(currencyTempSet))
                : currencyEvaluator.findBestSellCourses(currencyTempSet);
        sendResult(best);
      } catch (XmlPullParserException | IOException e) {
        ExceptionHandler.handleException(e);
      }
    }
  }

  private void sendResult(List<BestCourse> result) {
    Intent intent = new Intent(Constants.BROADCAST_ACTION_SUCCESS);
    intent.putParcelableArrayListExtra(Constants.EXTRA_BESTCOURSES,
        (ArrayList<? extends Parcelable>) result);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }

  private void sendError() {
    LocalBroadcastManager.getInstance(this)
        .sendBroadcast(new Intent(Constants.BROADCAST_ACTION_ERROR));
  }

  private void setupCitiesMap() {
    citiesMap.put("Минск", 1);
    citiesMap.put("Витебск", 2);
    citiesMap.put("Гомель", 3);
    citiesMap.put("Гродно", 4);
    citiesMap.put("Брест", 5);
    citiesMap.put("Могилёв", 6);
  }
}
