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

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.fobo66.valiutchik.desktop.di.refreshModule
import dev.fobo66.valiutchik.desktop.log.JvmAntilog
import dev.fobo66.valiutchik.presentation.di.viewModelsModule
import dev.fobo66.valiutchik.ui.TAG_SNACKBAR
import dev.fobo66.valiutchik.ui.preferences.PreferencesPanel
import dev.fobo66.valiutchik.ui.rates.RatesPanel
import dev.fobo66.valiutchik.ui.theme.AppTheme
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
            Window(onCloseRequest = { isSettingsOpen = false }, title = "Settings") {
                AppTheme {
                    Scaffold {
                        PreferencesPanel(
                            canOpenSettings = false,
                            onOpenLicenses = {},
                            onBack = { isSettingsOpen = false }
                        )
                    }
                }
            }
        }

        Window(onCloseRequest = ::exitApplication, title = "Valiutchik") {
            val snackbarHostState = remember { SnackbarHostState() }

            AppTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.navigationBarsPadding(),
                            snackbar = {
                                Snackbar(
                                    snackbarData = it,
                                    modifier = Modifier.testTag(TAG_SNACKBAR)
                                )
                            }
                        )
                    }
                ) {
                    RatesPanel(
                        snackbarHostState = snackbarHostState,
                        manualRefreshVisible = true,
                        canOpenSettings = true,
                        onOpenSettings = { isSettingsOpen = true },
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}
