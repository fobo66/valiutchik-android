package fobo66.exchangecourcesbelarus.model

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.core.app.JobIntentService
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fobo66.exchangecourcesbelarus.di.injector
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.datasource.CacheDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.util.ACTION_FETCH_COURSES
import fobo66.exchangecourcesbelarus.util.BROADCAST_ACTION_ERROR
import fobo66.exchangecourcesbelarus.util.BROADCAST_ACTION_SUCCESS
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import fobo66.exchangecourcesbelarus.util.EXTRA_BESTCOURSES
import fobo66.exchangecourcesbelarus.util.EXTRA_BUYORSELL
import fobo66.exchangecourcesbelarus.util.EXTRA_CITY
import fobo66.exchangecourcesbelarus.util.ExceptionHandler
import fobo66.exchangecourcesbelarus.util.TEMPLATE_URI
import fobo66.exchangecourcesbelarus.util.TIMESTAMP
import kotlinx.coroutines.launch
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.threeten.bp.LocalDateTime
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit.HOURS
import javax.inject.Inject

class CurrencyRateService : JobIntentService(), LifecycleOwner {

  @Inject
  lateinit var prefs: SharedPreferences

  @Inject
  lateinit var client: OkHttpClient

  @Inject
  lateinit var currencyEvaluator: CurrencyEvaluator

  @Inject
  lateinit var parser: CurrencyRatesParser

  @Inject
  lateinit var cacheDataSource: CacheDataSource

  @Inject
  lateinit var persistenceDataSource: PersistenceDataSource

  private val citiesMap: Map<String, Int> = mapOf(
    "Минск" to 1,
    "Витебск" to 2,
    "Гомель" to 3,
    "Гродно" to 4,
    "Брест" to 5,
    "Могилёв" to 6
  )
  private var buyOrSell = false

  private val lifecycleDispatcher = ServiceLifecycleDispatcher(this)

  override fun onCreate() {
    lifecycleDispatcher.onServicePreSuperOnCreate()
    injector.inject(this)
    super.onCreate()
  }

  override fun onBind(intent: Intent): IBinder? {
    lifecycleDispatcher.onServicePreSuperOnBind()
    return super.onBind(intent)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    lifecycleDispatcher.onServicePreSuperOnStart()
    return super.onStartCommand(intent, flags, startId)
  }

  @CallSuper
  override fun onDestroy() {
    lifecycleDispatcher.onServicePreSuperOnDestroy()
    super.onDestroy()
  }

  override fun onHandleWork(intent: Intent) {
    if (ACTION_FETCH_COURSES == intent.action) {
      val city = intent.getStringExtra(EXTRA_CITY)
      buyOrSell = intent.getBooleanExtra(EXTRA_BUYORSELL, false)
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
        TIMESTAMP,
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
              cacheResponse(response)
              saveTimestamp()
              tryReadCached()
            } else {
              sendError()
            }
          }
        })
      } else {
        tryReadCached()
      }
    }
  }

  private fun cacheResponse(response: Response) = lifecycleScope.launch {
    cacheDataSource.writeToCache(response.body?.source())
  }

  private fun tryReadCached() {
    try {
      readCached()
    } catch (e: Exception) {
      ExceptionHandler.handleException(e)
      sendError()
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

    return String.format(Locale.getDefault(), TEMPLATE_URI, cityIndex)
  }

  private fun saveTimestamp() {
    prefs.edit {
      putLong(TIMESTAMP, System.currentTimeMillis())
    }
  }

  private fun readCached() = lifecycleScope.launch {
    cacheDataSource.readCached {
      val timestamp = LocalDateTime.now().toString()
      val entries = parser.parse(this)
      val best =
        if (buyOrSell) {
          currencyEvaluator.findBestBuyCourses(entries, timestamp)
        } else {
          currencyEvaluator.findBestSellCourses(entries, timestamp)
        }
      saveResult(best)
      sendResult(best)
    }
  }

  private fun saveResult(bestCourses: List<BestCourse>) = lifecycleScope.launch {
    persistenceDataSource.saveBestCourses(bestCourses)
  }

  private fun sendResult(result: List<BestCourse>) {
    val intent = Intent(BROADCAST_ACTION_SUCCESS)
    intent.putParcelableArrayListExtra(
      EXTRA_BESTCOURSES,
      result as ArrayList<out Parcelable?>
    )
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
  }

  private fun sendError() {
    LocalBroadcastManager.getInstance(this)
      .sendBroadcast(Intent(BROADCAST_ACTION_ERROR))
  }

  companion object {
    private const val JOB_ID = 228
    private const val MAX_STALE_PERIOD: Long = 10800000 // 3 hours in ms

    fun fetchCourses(context: Context, city: String?, buyOrSell: Boolean) {
      val intent = Intent(context, CurrencyRateService::class.java).apply {
        action = ACTION_FETCH_COURSES
        putExtra(EXTRA_CITY, city)
        putExtra(EXTRA_BUYORSELL, buyOrSell)
      }

      enqueueWork(
        context,
        CurrencyRateService::class.java,
        JOB_ID,
        intent
      )
    }
  }

  override fun getLifecycle(): Lifecycle = lifecycleDispatcher.lifecycle
}