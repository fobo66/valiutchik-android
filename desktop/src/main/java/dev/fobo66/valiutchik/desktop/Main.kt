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

package dev.fobo66.valiutchik.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.fobo66.valiutchik.desktop.di.refreshModule
import dev.fobo66.valiutchik.desktop.log.JvmAntilog
import dev.fobo66.valiutchik.presentation.di.viewModelsModule
import dev.fobo66.valiutchik.ui.main.MainContent
import fobo66.valiutchik.domain.di.domainModule
import io.github.aakira.napier.Napier
import org.koin.compose.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
fun main() = application {
    LaunchedEffect(Unit) {
        Napier.base(JvmAntilog())
    }
    val state = rememberWindowState(size = DpSize(1920.dp, 1080.dp))
    Window(state = state, onCloseRequest = ::exitApplication, title = "Valiutchik") {
        KoinApplication(
            application = {
                modules(viewModelsModule, domainModule, refreshModule)
            }
        ) {
            MainContent()
        }
    }
}
