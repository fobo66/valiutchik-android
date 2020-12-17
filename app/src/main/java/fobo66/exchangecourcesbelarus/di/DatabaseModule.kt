package fobo66.exchangecourcesbelarus.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.exchangecourcesbelarus.db.CurrencyRatesDatabase
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/18/19.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): CurrencyRatesDatabase =
    Room.databaseBuilder(
      context,
      CurrencyRatesDatabase::class.java,
      "currency-rates"
    ).build()

  @Provides
  @Singleton
  @CacheDirectory
  fun provideCacheDirectory(@ApplicationContext context: Context): File = context.cacheDir
}

@Qualifier
@Retention(RUNTIME)
annotation class CacheDirectory