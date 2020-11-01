package fobo66.exchangecourcesbelarus.di

import android.content.Context
import dagger.Module
import dagger.Provides
import fobo66.exchangecourcesbelarus.BuildConfig
import fobo66.exchangecourcesbelarus.R
import fobo66.valiutchik.core.BASE_URL
import fobo66.exchangecourcesbelarus.util.CertificateManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Module
object NetworkModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(
    context: Context,
    certificateManager: CertificateManager,
    loggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    certificateManager.createTrustManagerForCertificate(
      context.resources.openRawResource(R.raw.myfinbynew)
    )

    return OkHttpClient.Builder().cache(Cache(context.cacheDir, 1024 * 1024 * 5))
      .addInterceptor(loggingInterceptor)
      .sslSocketFactory(
        certificateManager.trustedSocketFactory,
        certificateManager.trustManager
      )
      .build()
  }

  @Provides
  @Singleton
  fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.d(message) }

    loggingInterceptor.level = if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor.Level.BODY
    } else {
      HttpLoggingInterceptor.Level.NONE
    }

    return loggingInterceptor
  }

  @Provides
  @BaseUrl
  fun provideBaseUrl(): String = BASE_URL
}

@Qualifier
@Retention(SOURCE)
annotation class BaseUrl