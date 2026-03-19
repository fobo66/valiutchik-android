/*
 *    Copyright 2026 Andrey Mukamolov
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

package dev.fobo66.valiutchik.web

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import dev.fobo66.valiutchik.presentation.di.viewModelsModule
import dev.fobo66.valiutchik.ui.main.MainContent
import dev.fobo66.valiutchik.ui.theme.AppTheme
import dev.fobo66.valiutchik.web.di.refreshModule
import fobo66.valiutchik.domain.di.domainModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.compose.resources.configureWebResources
import org.koin.compose.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

@OptIn(
    KoinExperimentalAPI::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalComposeUiApi::class
)
fun main() {
    configureWebResources { resourcePathMapping { path -> "./$path" } }
    ComposeViewport {
        LaunchedEffect(Unit) {
            Napier.base(DebugAntilog())
        }

        KoinApplication(
            configuration = koinConfiguration(declaration = {
                modules(
                    viewModelsModule,
                    domainModule,
                    refreshModule
                )
            }),
            content = {
                AppTheme {
                    MainContent(showManualRefresh = true, modifier = Modifier.fillMaxSize())
                }
            }
        )
    }
}
