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

package dev.fobo66.valiutchik.ui.rates

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import dev.fobo66.valiutchik.presentation.MainViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import valiutchik.ui.generated.resources.Res
import valiutchik.ui.generated.resources.currency_value_copied
import valiutchik.ui.generated.resources.get_data_error
import valiutchik.ui.generated.resources.maps_app_required
import valiutchik.ui.generated.resources.permission_action
import valiutchik.ui.generated.resources.permission_description

@Composable
fun RatesPanel(
    snackbarHostState: SnackbarHostState,
    onOpenSettings: suspend () -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = metroViewModel(),
    permissionPrompt: String = stringResource(Res.string.permission_description),
    errorMessage: String = stringResource(Res.string.get_data_error),
    rateCopiedMessage: String = stringResource(Res.string.currency_value_copied),
    noMapMessage: String = stringResource(Res.string.maps_app_required),
    permissionAction: String = stringResource(Res.string.permission_action)
) {
    val scope = rememberCoroutineScope()
    val actualOpenSettings by rememberUpdatedState(onOpenSettings)

    CircuitContent(RatesScreen, modifier = modifier, onNavEvent = {
        if (it is NavEvent.Forward) {
            scope.launch {
                actualOpenSettings()
            }
        }
    })
}

private fun handleOpenMap(
    isMapOpened: Boolean,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    noMapMessage: String
) {
    if (!isMapOpened) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = noMapMessage
            )
        }
    }
}
