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
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */

const val BASE_URL = "https://admin.myfin.by/"
const val CACHE_SIZE = 1024L * 1024L * 2L

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

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
