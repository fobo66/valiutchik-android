package fobo66.exchangecourcesbelarus.di

import android.content.Context
import dagger.Module
import dagger.Provides
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.util.CertificateManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Module
object NetworkModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(context: Context, certificateManager: CertificateManager): OkHttpClient {
    certificateManager.createTrustManagerForCertificate(context.resources.openRawResource(R.raw.myfinby))

    return OkHttpClient.Builder().cache(Cache(context.cacheDir, 1024 * 1024 * 5))
      .sslSocketFactory(
        certificateManager.getTrustedSocketFactory(),
        certificateManager.getTrustManager()
      )
      .build()
  }
}