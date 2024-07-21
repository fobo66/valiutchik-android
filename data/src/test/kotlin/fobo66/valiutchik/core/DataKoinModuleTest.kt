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

package fobo66.valiutchik.core

import fobo66.valiutchik.core.di.coroutineDispatchersModule
import fobo66.valiutchik.core.di.dataSourcesModule
import fobo66.valiutchik.core.di.repositoriesModule
import fobo66.valiutchik.core.di.secretsModule
import fobo66.valiutchik.core.di.systemModule
import fobo66.valiutchik.core.di.thirdPartyModule
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import org.junit.jupiter.api.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class DataKoinModuleTest {

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun `check dispatchers module`() {
    coroutineDispatchersModule.verify()
  }

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun `check secrets module`() {
    secretsModule.verify()
  }

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun `check system module`() {
    systemModule.verify()
  }

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun `check third party module`() {
    thirdPartyModule.verify()
  }

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun `check datasource module`() {
    dataSourcesModule.verify(
      extraTypes = listOf(
        HttpClientEngine::class,
        HttpClientConfig::class
      )
    )
  }

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun `check repositories module`() {
    repositoriesModule.verify(
      extraTypes = listOf(
        HttpClientEngine::class,
        HttpClientConfig::class
      )
    )
  }
}
