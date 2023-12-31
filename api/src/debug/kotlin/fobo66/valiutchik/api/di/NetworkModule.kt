/*
 *    Copyright 2023 Andrey Mukamolov
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

package fobo66.valiutchik.api.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.api.ExchangeRatesApi
import fobo66.valiutchik.api.RequestConfigInterceptor
import fobo66.valiutchik.api.XmlConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber

const val BASE_URL = "https://admin.myfin.by/"
const val CACHE_SIZE = 1024L * 1024L * 2L

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

  @Provides
  fun provideKtorClient(
    @ApplicationContext context: Context,
    @ApiUsername username: String,
    @ApiPassword password: String
  ) = HttpClient(OkHttp) {
    install(Auth) {
      basic {
        credentials {
          BasicAuthCredentials(username, password)
        }
      }
    }
    install(HttpCache) {
      publicStorage(FileStorage(context.cacheDir))
    }
    install(Logging) {
      logger = object: Logger {
        override fun log(message: String) {
          Timber.tag("HTTP").d(message)
        }
      }
      level = LogLevel.ALL
      sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }

    expectSuccess = true
  }

  @Provides
  @Singleton
  fun provideMyfinApi(
    okHttpClient: OkHttpClient,
    xmlConverterFactory: XmlConverterFactory
  ): ExchangeRatesApi = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(xmlConverterFactory)
    .build()
    .create()

  @Provides
  @Singleton
  fun provideOkHttpClient(
    @ApplicationContext context: Context,
    loggingInterceptor: HttpLoggingInterceptor,
    requestConfigInterceptor: RequestConfigInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder().cache(Cache(context.cacheDir, CACHE_SIZE))
      .addInterceptor(requestConfigInterceptor)
      .addInterceptor(loggingInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }

    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    return loggingInterceptor
  }
}
