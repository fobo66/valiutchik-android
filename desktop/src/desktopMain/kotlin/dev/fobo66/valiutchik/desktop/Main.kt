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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import dev.fobo66.valiutchik.desktop.di.refreshModule
import dev.fobo66.valiutchik.desktop.log.JvmAntilog
import dev.fobo66.valiutchik.desktop.rates.RatesWindow
import dev.fobo66.valiutchik.desktop.settings.SettingsWindow
import dev.fobo66.valiutchik.presentation.di.viewModelsModule
import fobo66.valiutchik.domain.di.domainModule
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.test.setMain
import org.koin.compose.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalCoroutinesApi::class)
fun main() = application {
    LaunchedEffect(Unit) {
        Napier.base(JvmAntilog())
    }

    LaunchedEffect(Unit) {
        Dispatchers.setMain(Dispatchers.Swing)
    }

    KoinApplication(
        application = {
            modules(viewModelsModule, domainModule, refreshModule)
        }
    ) {
        var isSettingsOpen by remember { mutableStateOf(false) }
        if (isSettingsOpen) {
            SettingsWindow(onClose = { isSettingsOpen = false })
        }

        RatesWindow(onClose = ::exitApplication, onOpenSettings = { isSettingsOpen = true })
    }
}
