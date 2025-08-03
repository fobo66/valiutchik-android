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

package dev.fobo66.valiutchik.desktop.rates

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Window
import dev.fobo66.valiutchik.ui.TAG_SNACKBAR
import dev.fobo66.valiutchik.ui.icon.Bank
import dev.fobo66.valiutchik.ui.rates.RatesPanel
import dev.fobo66.valiutchik.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import valiutchik.desktop.generated.resources.Res
import valiutchik.desktop.generated.resources.app_name

@Composable
fun RatesWindow(onClose: () -> Unit, onOpenSettings: () -> Unit) {
    Window(
        icon = rememberVectorPainter(Icons.Default.Bank),
        onCloseRequest = onClose,
        title = stringResource(Res.string.app_name)
    ) {
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
                    onOpenSettings = onOpenSettings,
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}
