package fobo66.exchangecourcesbelarus.model

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcelable
import androidx.core.app.JobIntentService
import androidx.core.content.edit
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.di.injector
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.entities.Currency
import fobo66.exchangecourcesbelarus.util.Constants
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import fobo66.exchangecourcesbelarus.util.ExceptionHandler
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.io.IOUtils
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Locale
import java.util.concurrent.TimeUnit.HOURS
import javax.inject.Inject

class CurrencyRateService : JobIntentService() {

  @Inject
  lateinit var prefs: SharedPreferences

  @Inject
  lateinit var client: OkHttpClient

  private val citiesMap: Map<String, Int> = mapOf(
    "Минск" to 1,
    "Витебск" to 2,
    "Гомель" to 3,
    "Гродно" to 4,
    "Брест" to 5,
    "Могилёв" to 6
  )
  private var buyOrSell = false

  override fun onCreate() {
    injector.inject(this)
    super.onCreate()
  }

  override fun onHandleWork(intent: Intent) {
    if (Constants.ACTION_FETCH_COURSES == intent.action) {
      val city = intent.getStringExtra(Constants.EXTRA_CITY)
      buyOrSell = intent.getBooleanExtra(Constants.EXTRA_BUYORSELL, false)
      resolveBestCurrencyRates(city)
    }
  }

  /**
   * Handle action in the provided background thread with the provided
   * parameters.
   */
  private fun resolveBestCurrencyRates(city: String?) {
    val url: String
    val timeStamp: Long
    val currentTime = System.currentTimeMillis()
    if (city != null) {
      timeStamp = prefs.getLong(
        Constants.TIMESTAMP,
        currentTime - MAX_STALE_PERIOD
      )
      if (currentTime - timeStamp >= MAX_STALE_PERIOD) {
        url = getUrlForCity(city)
        val request = prepareRequest(url)
        client.newCall(request).enqueue(object : Callback {
          override fun onFailure(call: Call, e: IOException) {
            ExceptionHandler.handleException(e)
            sendError()
          }

          @Throws(IOException::class)
          override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
              val responseCharStream = response.body!!.charStream()
              val xmlCache = File(cacheDir, getString(string.feed_xml_name))
              xmlCache.createNewFile()
              val xmlStream: OutputStream = FileOutputStream(xmlCache)
              try {
                IOUtils.copy(responseCharStream, xmlStream)
                saveTimestamp()
                readCached()
              } finally {
                xmlStream.close()
                responseCharStream.close()
              }
            }
          }
        })
      }
      try {
        readCached()
      } catch (e: Exception) {
        ExceptionHandler.handleException(e)
        sendError()
      }
    }
  }

  private fun prepareRequest(url: String): Request {
    val credential = Credentials.basic("app", "android")
    return Request.Builder().url(url)
      .addHeader("Authorization", credential)
      .cacheControl(CacheControl.Builder().maxAge(3, HOURS).build())
      .build()
  }

  private fun getUrlForCity(city: String): String {
    val cityIndex = citiesMap[city] ?: 1

    return String.format(Locale.getDefault(), Constants.TEMPLATE_URI, cityIndex)
  }

  private fun saveTimestamp() {
    prefs.edit {
      putLong(Constants.TIMESTAMP, System.currentTimeMillis())
    }
  }

  private fun readCached() {
    val cache = File(cacheDir, getString(string.feed_xml_name))
    if (cache.exists() && cache.length() > 0) {
      try {
        FileInputStream(cache).use { cachedStream ->
          val currencyEvaluator = CurrencyEvaluator()
          val parser: CurrencyRatesParser = MyfinParser()
          val entries = parser.parse(cachedStream)
          val currencyTempSet: Set<Currency> = HashSet(entries)
          val best =
            if (buyOrSell) currencyEvaluator.findBestBuyCourses(currencyTempSet) else currencyEvaluator.findBestSellCourses(
              currencyTempSet
            )
          sendResult(best)
        }
      } catch (e: XmlPullParserException) {
        ExceptionHandler.handleException(e)
      } catch (e: IOException) {
        ExceptionHandler.handleException(e)
      }
    }
  }

  private fun sendResult(result: List<BestCourse?>) {
    val intent = Intent(Constants.BROADCAST_ACTION_SUCCESS)
    intent.putParcelableArrayListExtra(
      Constants.EXTRA_BESTCOURSES,
      result as ArrayList<out Parcelable?>
    )
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
  }

  private fun sendError() {
    LocalBroadcastManager.getInstance(this)
      .sendBroadcast(Intent(Constants.BROADCAST_ACTION_ERROR))
  }

  companion object {
    private const val JOB_ID = 228
    private const val MAX_STALE_PERIOD: Long = 10800000 // 3 hours in ms

    fun fetchCourses(context: Context, city: String?, buyOrSell: Boolean) {
      val intent = Intent(context, CurrencyRateService::class.java)
      intent.action = Constants.ACTION_FETCH_COURSES
      intent.putExtra(Constants.EXTRA_CITY, city)
      intent.putExtra(Constants.EXTRA_BUYORSELL, buyOrSell)
      enqueueWork(
        context,
        CurrencyRateService::class.java,
        JOB_ID,
        intent
      )
    }
  }
}