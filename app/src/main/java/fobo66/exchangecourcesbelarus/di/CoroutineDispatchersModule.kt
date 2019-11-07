package fobo66.exchangecourcesbelarus.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Module
object CoroutineDispatchersModule {
  const val IO = "io"
  const val MAIN = "main"

  @Provides
  @Named(IO)
  fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

  @Provides
  @Named(MAIN)
  fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}