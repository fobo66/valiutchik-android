package fobo66.exchangecourcesbelarus.api

import fobo66.exchangecourcesbelarus.di.ApiPassword
import fobo66.exchangecourcesbelarus.di.ApiUsername
import java.util.concurrent.TimeUnit.HOURS
import javax.inject.Inject
import okhttp3.CacheControl
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class RequestConfigInterceptor @Inject constructor(
  @ApiUsername private val username: String,
  @ApiPassword private val password: String
) : Interceptor {
  private val credential by lazy { Credentials.basic(username, password) }

  override fun intercept(chain: Chain): Response {
    val newRequest = chain.request().newBuilder()
      .addHeader("Authorization", credential)
      .cacheControl(CacheControl.Builder().maxAge(CACHE_MAX_AGE, HOURS).build())
      .build()

    return chain.proceed(newRequest)
  }

  companion object {
    const val CACHE_MAX_AGE = 3
  }
}
