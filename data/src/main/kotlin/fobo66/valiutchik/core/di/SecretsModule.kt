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

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.core.R
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal enum class Secret {
  GEOCODER_ACCESS_TOKEN
}

@Module
@InstallIn(SingletonComponent::class)
object SecretsModule {

  @GeocoderAccessToken
  @Provides
  fun provideGeocoderAccessToken(@ApplicationContext context: Context): String =
    context.getString(R.string.geocoderAccessToken)
}

val secretsModule = module {
  single(qualifier(Secret.GEOCODER_ACCESS_TOKEN)) {
    androidContext().getString(R.string.geocoderAccessToken)
  }
}
