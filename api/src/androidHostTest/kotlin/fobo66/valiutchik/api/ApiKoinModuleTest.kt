/*
 *    Copyright 2025 Andrey Mukamolov
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

package fobo66.valiutchik.api

import fobo66.valiutchik.api.di.apiModule
import fobo66.valiutchik.api.di.credentialsModule
import fobo66.valiutchik.api.di.dispatchersModule
import fobo66.valiutchik.api.di.networkModule
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlin.test.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class ApiKoinModuleTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `check dispatchers module`() {
        dispatchersModule.verify()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `check network module`() {
        networkModule.verify(
            extraTypes =
            listOf(
                HttpClientEngine::class,
                HttpClientConfig::class
            )
        )
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `check credentials module`() {
        credentialsModule.verify()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `check api module`() {
        apiModule.verify(
            extraTypes =
            listOf(
                HttpClientEngine::class,
                HttpClientConfig::class
            )
        )
    }
}
