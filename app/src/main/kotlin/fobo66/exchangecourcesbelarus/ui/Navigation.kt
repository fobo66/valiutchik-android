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

package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.fobo66.valiutchik.presentation.MainViewModel
import dev.fobo66.valiutchik.presentation.entity.MainScreenState
import dev.fobo66.valiutchik.ui.rates.BestRatesGrid
import fobo66.exchangecourcesbelarus.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BestRatesScreenDestination(
    navigator: ThreePaneScaffoldNavigator<Any>,
    snackbarHostState: SnackbarHostState,
    manualRefreshVisible: Boolean,
    canOpenSettings: Boolean,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = koinViewModel(),
    permissionPrompt: String = stringResource(R.string.permission_description),
    errorMessage: String = stringResource(R.string.get_data_error),
    rateCopiedMessage: String = stringResource(R.string.currency_value_copied),
    noMapMessage: String = stringResource(R.string.maps_app_required),
    permissionAction: String = "Grant"
) {
    val context = LocalContext.current
    val bestCurrencyRates by mainViewModel.bestCurrencyRates.collectAsStateWithLifecycle()

    val viewState by mainViewModel.screenState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

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
            shareCurrencyRate(context, currencyName, currencyValue)
        },
        showExplicitRefresh = manualRefreshVisible,
        showSettings = canOpenSettings,
        onSettingsClick = {
            scope.launch {
                navigator.navigateTo(ThreePaneScaffoldRole.Secondary)
            }
        },
        isRefreshing = viewState is MainScreenState.Loading,
        onRefresh = mainViewModel::manualRefresh,
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PermissionsEffect(
    snackbarHostState: SnackbarHostState,
    permissionPrompt: String,
    permissionAction: String,
    onHandlePermissions: (Boolean) -> Unit
) {
    var isLocationPermissionPromptShown by rememberSaveable { mutableStateOf(false) }
    val permissionState = rememberPermissionState(permission.ACCESS_COARSE_LOCATION)
    val permissionsHandler by rememberUpdatedState(onHandlePermissions)

    LaunchedEffect(permissionState.status) {
        val isPermissionGranted = permissionState.status.isGranted
        permissionsHandler(isPermissionGranted)
        if (!isPermissionGranted && !isLocationPermissionPromptShown) {
            isLocationPermissionPromptShown = true
            val result = snackbarHostState.showSnackbar(
                message = permissionPrompt,
                withDismissAction = true,
                actionLabel = permissionAction
            )

            if (result == SnackbarResult.ActionPerformed) {
                permissionState.launchPermissionRequest()
            }
        }
    }
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

private fun shareCurrencyRate(context: Context, currencyName: String, currencyValue: String) {
    val shareIntent =
        Intent(Intent.ACTION_SEND)
            .putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.share_rate_text, currencyName, currencyValue)
            ).setType("text/plain")
    val sender =
        Intent.createChooser(shareIntent, context.getString(R.string.share_rate, currencyName))
    context.startActivity(sender)
}
