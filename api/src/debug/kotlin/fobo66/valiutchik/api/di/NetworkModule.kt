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

package fobo66.valiutchik.api.di

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val networkModule = module {
  single<Logger> {
    object : Logger {
      override fun log(message: String) {
        Napier.d(message, tag = "Ktor")
      }
    }
  }
  single<HttpClient> {
    HttpClient(OkHttp) {
      install(Auth) {
        basic {
          credentials {
            BasicAuthCredentials(get(qualifier(Api.USERNAME)), get(qualifier(Api.PASSWORD)))
          }
        }
      }
      install(HttpCache) {
        publicStorage(FileStorage(androidContext().cacheDir))
      }
      install(Logging) {
        logger = get()
        level = LogLevel.ALL
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
      }

      expectSuccess = true
    }
  }
}
