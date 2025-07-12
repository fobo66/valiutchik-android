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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Long
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.fobo66.valiutchik.presentation.MainViewModel
import dev.fobo66.valiutchik.presentation.OpenSourceLicensesViewModel
import dev.fobo66.valiutchik.presentation.entity.MainScreenState
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesScreen
import fobo66.exchangecourcesbelarus.ui.main.BestRatesGrid
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreen
import fobo66.exchangecourcesbelarus.ui.preferences.PreferencesViewModel
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
    mainViewModel: MainViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val bestCurrencyRates by mainViewModel.bestCurrencyRates.collectAsStateWithLifecycle()

    val viewState by mainViewModel.screenState.collectAsStateWithLifecycle()

    var isLocationPermissionPromptShown by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val permissionState = rememberPermissionState(permission.ACCESS_COARSE_LOCATION)
    LaunchedEffect(Unit) { permissionState.launchPermissionRequest() }

    LaunchedEffect(permissionState.status) {
        val isPermissionGranted = permissionState.status.isGranted
        mainViewModel.handleLocationPermission(isPermissionGranted)
        if (!isPermissionGranted && !isLocationPermissionPromptShown) {
            isLocationPermissionPromptShown = true
            showSnackbar(
                snackbarHostState,
                context.getString(R.string.permission_description),
                Long
            )
        }
    }
    LaunchedEffect(viewState) {
        if (viewState is MainScreenState.Error) {
            showSnackbar(snackbarHostState, context.getString(R.string.get_data_error))
        }
    }
    BestRatesGrid(
        bestCurrencyRates = bestCurrencyRates,
        onBestRateClick = { bankName ->
            val isMapOpened = mainViewModel.findBankOnMap(bankName)
            handleOpenMap(isMapOpened, context, scope, snackbarHostState)
        },
        onBestRateLongClick = { currencyValue ->
            mainViewModel.copyCurrencyRateToClipboard(currencyValue)
            scope.launch {
                showSnackbar(snackbarHostState, context.getString(R.string.currency_value_copied))
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PreferenceScreenDestination(
    navigator: ThreePaneScaffoldNavigator<Any>,
    canOpenSettings: Boolean,
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val defaultCity by preferencesViewModel.defaultCityPreference
        .collectAsStateWithLifecycle()

    val updateInterval by preferencesViewModel.updateIntervalPreference
        .collectAsStateWithLifecycle()

    PreferenceScreen(
        defaultCityValue = defaultCity,
        updateIntervalValue = updateInterval,
        canOpenSettings = canOpenSettings,
        onDefaultCityChange = preferencesViewModel::updateDefaultCity,
        onUpdateIntervalChange = preferencesViewModel::updateUpdateInterval,
        onOpenSourceLicensesClick = {
            scope.launch {
                navigator.navigateTo(
                    ThreePaneScaffoldRole.Tertiary
                )
            }
        },
        onBackClick = {
            scope.launch {
                navigator.navigateBack()
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OpenSourceLicensesDestination(
    navigator: ThreePaneScaffoldNavigator<Any>,
    modifier: Modifier = Modifier,
    viewModel: OpenSourceLicensesViewModel = koinViewModel()
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    val licensesState by viewModel.licensesState.collectAsStateWithLifecycle()

    OpenSourceLicensesScreen(
        licensesState = licensesState,
        onItemClick = uriHandler::openUri,
        onBackClick = {
            scope.launch {
                navigator.navigateBack()
            }
        },
        modifier = modifier
    )
}

private fun handleOpenMap(
    isMapOpened: Boolean,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    if (!isMapOpened) {
        scope.launch {
            showSnackbar(snackbarHostState, context.getString(R.string.maps_app_required))
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

private suspend fun showSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    duration: SnackbarDuration = Short
) {
    snackbarHostState.showSnackbar(
        message = message,
        duration = duration
    )
}
