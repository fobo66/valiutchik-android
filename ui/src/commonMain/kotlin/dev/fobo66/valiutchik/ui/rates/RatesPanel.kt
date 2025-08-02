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

package dev.fobo66.valiutchik.ui.rates

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fobo66.valiutchik.presentation.MainViewModel
import dev.fobo66.valiutchik.presentation.entity.MainScreenState
import dev.fobo66.valiutchik.ui.share.rememberShareProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import valiutchik.ui.generated.resources.Res
import valiutchik.ui.generated.resources.currency_value_copied
import valiutchik.ui.generated.resources.get_data_error
import valiutchik.ui.generated.resources.maps_app_required
import valiutchik.ui.generated.resources.permission_action
import valiutchik.ui.generated.resources.permission_description

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RatesPanel(
    snackbarHostState: SnackbarHostState,
    manualRefreshVisible: Boolean,
    canOpenSettings: Boolean,
    onOpenSettings: suspend () -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = koinViewModel(),
    permissionPrompt: String = stringResource(Res.string.permission_description),
    errorMessage: String = stringResource(Res.string.get_data_error),
    rateCopiedMessage: String = stringResource(Res.string.currency_value_copied),
    noMapMessage: String = stringResource(Res.string.maps_app_required),
    permissionAction: String = stringResource(Res.string.permission_action)
) {
    val shareProvider = rememberShareProvider()
    val bestCurrencyRates by mainViewModel.bestCurrencyRates.collectAsStateWithLifecycle()

    val viewState by mainViewModel.screenState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val actualOpenSettings by rememberUpdatedState(onOpenSettings)

    PermissionsEffect(
        snackbarHostState,
        permissionPrompt,
        permissionAction
    ) {
        mainViewModel.handleLocationPermission(it)
    }

    LaunchedEffect(viewState) {
        if (viewState is MainScreenState.Error) {
            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }
    BestRatesGrid(
        bestCurrencyRates = bestCurrencyRates,
        onBestRateClick = { bankName ->
            val isMapOpened = mainViewModel.findBankOnMap(bankName)
            handleOpenMap(isMapOpened, scope, snackbarHostState, noMapMessage)
        },
        onBestRateLongClick = { currencyValue ->
            mainViewModel.copyCurrencyRateToClipboard(currencyValue)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = rateCopiedMessage
                )
            }
        },
        onShareClick = { currencyName, currencyValue ->
            shareProvider.shareText(currencyName, currencyValue)
        },
        showExplicitRefresh = manualRefreshVisible,
        showSettings = canOpenSettings,
        onSettingsClick = {
            scope.launch {
                actualOpenSettings()
            }
        },
        isRefreshing = viewState is MainScreenState.Loading,
        onRefresh = mainViewModel::manualRefresh,
        modifier = modifier
    )
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
