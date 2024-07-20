/*
 *    Copyright 2024 Andrey Mukamolov
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

package fobo66.valiutchik.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal enum class Dispatcher {
  IO
}

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

val coroutineDispatchersModule = module {
  single(qualifier(Dispatcher.IO)) { Dispatchers.IO }
}
