package fobo66.exchangecourcesbelarus;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.models.Currency;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static fobo66.exchangecourcesbelarus.MainActivity.TIMESTAMP;
import static fobo66.exchangecourcesbelarus.MainActivity.buyOrSell;

public class CurrencyRateService extends IntentService {

    private OkHttpClient client;
    private static final long MAX_STALE_PERIOD = 10800000; // 3 hours in ms
    private final long httpCacheSize = 1024 * 1024 * 5; // 5 MiB
    private SharedPreferences prefs;

    public CurrencyRateService() {
        super("CurrencyRateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        client = new OkHttpClient.Builder()
                .cache(new Cache(getCacheDir(), httpCacheSize))
                .build();

        prefs = getApplicationContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void fetchCourses(Context context, String city) {
        Intent intent = new Intent(context, CurrencyRateService.class);
        intent.setAction(Constants.ACTION_FETCH_COURSES);
        intent.putExtra(Constants.EXTRA_CITY, city);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_FETCH_COURSES.equals(action)) {
                final String city = intent.getStringExtra(Constants.EXTRA_CITY);
                resolveBestCurrencyRates(city);
            }
        }
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
            timeStamp = prefs.getLong(TIMESTAMP, currentTime - MAX_STALE_PERIOD);
            if ((currentTime - timeStamp) >= MAX_STALE_PERIOD) {
                switch (city) {
                    case "Могилёв":
                        url = Constants.MOGILEV_URI;
                        break;
                    case "Минск":
                        url = Constants.MINSK_URI;
                        break;
                    case "Витебск":
                        url = Constants.VITEBSK_URI;
                        break;
                    case "Гомель":
                        url = Constants.GOMEL_URI;
                        break;
                    case "Брест":
                        url = Constants.BREST_URI;
                        break;
                    case "Гродно":
                        url = Constants.GRODNO_URI;
                        break;
                    default:
                        url = Constants.MINSK_URI;
                }
                String credential = Credentials.basic("app", "android");
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", credential)
                        .cacheControl(new CacheControl.Builder()
                                .maxAge(3, TimeUnit.HOURS)
                                .build())
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ExceptionHandler.handleException(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Reader responseCharStream = response.body().charStream();
                            File xmlCache = new File(getCacheDir(), getString(R.string.feed_xml_name));
                            xmlCache.createNewFile();
                            OutputStream xmlStream = new FileOutputStream(xmlCache);
                            try {
                                IOUtils.copy(responseCharStream, xmlStream);
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
            }
        }
}

    private void saveTimestamp() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    private void readCached() throws IOException {
        File cache = new File(getCacheDir(), getString(R.string.feed_xml_name));
        if (cache.exists() && cache.length() > 0) {
            InputStream cachedStream = new FileInputStream(cache);
            try {
                CurrencyEvaluator currencyEvaluator = new CurrencyEvaluator();
                CurrencyCourseParser parser = new MyfinXMLParser();
                List<Currency> entries = parser.parse(cachedStream);
                Set<Currency> currencyTempSet = new HashSet<>(entries);
                final List<BestCourse> best = (buyOrSell) ? (currencyEvaluator.findBestBuyCourses(currencyTempSet))
                        : currencyEvaluator.findBestSellCourses(currencyTempSet);
                sendResult(best);
            } catch (XmlPullParserException | IOException e) {
                ExceptionHandler.handleException(e);
            } finally {
                cachedStream.close();
            }
        }
    }

    private void sendResult(List<BestCourse> result) {
        Intent intent = new Intent(Constants.BROADCAST_ACTION);
        intent.putParcelableArrayListExtra(Constants.EXTRA_BESTCOURSES, (ArrayList<? extends Parcelable>) result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    }
