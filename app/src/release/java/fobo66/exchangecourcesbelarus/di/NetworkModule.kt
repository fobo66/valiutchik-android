package fobo66.exchangecourcesbelarus.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.core.BASE_URL
import fobo66.valiutchik.core.CACHE_SIZE
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(
    @ApplicationContext context: Context
  ): OkHttpClient {
    return OkHttpClient.Builder().cache(Cache(context.cacheDir, CACHE_SIZE))
      .build()
  }

  @Provides
  @BaseUrl
  fun provideBaseUrl(): String = BASE_URL
}
