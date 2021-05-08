package fobo66.exchangecourcesbelarus.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

  @Provides
  @Io
  fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

  @Provides
  @Main
  fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Qualifier
@Retention(RUNTIME)
annotation class Io

@Qualifier
@Retention(RUNTIME)
annotation class Main
