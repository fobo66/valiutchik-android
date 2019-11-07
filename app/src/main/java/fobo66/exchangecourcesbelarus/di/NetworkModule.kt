package fobo66.exchangecourcesbelarus.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import fobo66.exchangecourcesbelarus.BuildConfig
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.util.CertificateManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

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
    certificateManager.createTrustManagerForCertificate(context.resources.openRawResource(R.raw.myfinby))

    return OkHttpClient.Builder().cache(Cache(context.cacheDir, 1024 * 1024 * 5))
      .addInterceptor(loggingInterceptor)
      .sslSocketFactory(
        certificateManager.getTrustedSocketFactory(),
        certificateManager.getTrustManager()
      )
      .build()
  }

  @Provides
  @Singleton
  fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor(
      HttpLoggingInterceptor.Logger { message -> Log.d("OkHttp", message) }
    )

    loggingInterceptor.level = if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor.Level.BODY
    } else {
      HttpLoggingInterceptor.Level.NONE
    }

    return loggingInterceptor
  }
}