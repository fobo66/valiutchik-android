package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.util.BASE_URL
import fobo66.exchangecourcesbelarus.util.await
import okhttp3.CacheControl
import okhttp3.Credentials
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit.HOURS
import javax.inject.Inject

interface CurrencyRatesDataSource {
  suspend fun loadExchangeRates(city: String): Response
}

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class MyfinDataSource @Inject constructor(
  private val client: OkHttpClient,
  private val baseUrl: String = BASE_URL
) : CurrencyRatesDataSource {

  private val citiesMap: Map<String, String> = mapOf(
    "Минск" to "1",
    "Витебск" to "2",
    "Гомель" to "3",
    "Гродно" to "4",
    "Брест" to "5",
    "Могилёв" to "6"
  )

  override suspend fun loadExchangeRates(city: String): Response {
    val request = prepareRequest(resolveUrl(city))

    return client.newCall(request).await()
  }

  private fun prepareRequest(url: HttpUrl): Request {
    val credential = Credentials.basic("app", "android")
    return Request.Builder().url(url)
      .addHeader("Authorization", credential)
      .cacheControl(CacheControl.Builder().maxAge(3, HOURS).build())
      .build()
  }

  private fun resolveUrl(city: String): HttpUrl {
    val cityIndex = citiesMap[city] ?: "1"

    return baseUrl.toHttpUrl().newBuilder()
      .addPathSegment(cityIndex)
      .build()
  }
}