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

package dev.fobo66.valiutchik.desktop.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import dev.fobo66.valiutchik.ui.licenses.OpenSourceLicensesPanel
import dev.fobo66.valiutchik.ui.preferences.PreferencesPanel
import dev.fobo66.valiutchik.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import valiutchik.desktop.generated.resources.Res
import valiutchik.desktop.generated.resources.title_settings

@Composable
fun SettingsWindow(onClose: () -> Unit) {
    Window(
        icon = rememberVectorPainter(Icons.Default.Settings),
        onCloseRequest = onClose,
        title = stringResource(Res.string.title_settings)
    ) {
        AppTheme {
            Scaffold { paddingValues ->
                var isLicensesShown by remember { mutableStateOf(false) }
                Crossfade(isLicensesShown, modifier = Modifier.padding(paddingValues)) {
                    if (it) {
                        OpenSourceLicensesPanel(
                            onBack = { isLicensesShown = false }
                        )
                    } else {
                        PreferencesPanel(
                            canOpenSettings = false,
                            onOpenLicenses = { isLicensesShown = true },
                            onBack = onClose
                        )
                    }
                }
            }
        }
    }
}
