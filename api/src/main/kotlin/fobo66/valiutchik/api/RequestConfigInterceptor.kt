/*
 *    Copyright 2022 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.api

import fobo66.valiutchik.api.di.ApiPassword
import fobo66.valiutchik.api.di.ApiUsername
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
