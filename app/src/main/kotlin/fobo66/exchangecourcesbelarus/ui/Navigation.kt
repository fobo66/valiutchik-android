/*
 *    Copyright 2024 Andrey Mukamolov
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

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Long
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.entities.MainScreenState
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesScreen
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesViewModel
import fobo66.exchangecourcesbelarus.ui.main.BestRatesGrid
import fobo66.exchangecourcesbelarus.ui.main.BestRatesList
import fobo66.exchangecourcesbelarus.ui.preferences.MIN_UPDATE_INTERVAL_VALUE
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreenContent
import fobo66.exchangecourcesbelarus.ui.preferences.PreferencesViewModel
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val DESTINATION_MAIN = "main"
const val DESTINATION_PREFERENCES = "prefs"
const val DESTINATION_LICENSES = "licenses"

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.bestRatesScreen(
  snackbarHostState: SnackbarHostState,
  useGrid: Boolean = false,
  mainViewModel: MainViewModel,
  permissionState: PermissionState
) {
  composable(DESTINATION_MAIN) {
    val context = LocalContext.current
    val mapLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Napier.d("Opened map")
      }

    val bestCurrencyRates by mainViewModel.bestCurrencyRates.collectAsStateWithLifecycle()

    val viewState by mainViewModel.screenState.collectAsStateWithLifecycle(
      initialValue = MainScreenState.Loading
    )

    var isLocationPermissionPromptShown by rememberSaveable {
      mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
      permissionState.launchPermissionRequest()
    }

    LaunchedEffect(permissionState) {
      if (permissionState.status is PermissionStatus.Granted) {
        mainViewModel.refreshExchangeRates()
      } else {
        if (!isLocationPermissionPromptShown) {
          isLocationPermissionPromptShown = true
          showSnackbar(snackbarHostState, context.getString(string.permission_description), Long)
        }
        mainViewModel.refreshExchangeRatesForDefaultCity()
      }
    }
    LaunchedEffect(viewState) {
      if (viewState is MainScreenState.Error) {
        showSnackbar(snackbarHostState, context.getString(string.get_data_error))
      }
    }
    BestRatesScreen(
      bestCurrencyRates = bestCurrencyRates,
      onBestRateClick = { bankName ->
        val mapIntent = mainViewModel.findBankOnMap(bankName)
        if (mapIntent != null) {
          mapLauncher.launch(
            Intent.createChooser(mapIntent, context.getString(string.open_map))
          )
        } else {
          scope.launch {
            showSnackbar(snackbarHostState, context.getString(string.maps_app_required))
          }
        }
      },
      onBestRateLongClick = { currencyName, currencyValue ->
        mainViewModel.copyCurrencyRateToClipboard(currencyName, currencyValue)
        scope.launch {
          showSnackbar(snackbarHostState, context.getString(string.currency_value_copied))
        }
      },
      useGrid = useGrid
    )
  }
}

@Composable
fun BestRatesScreen(
  bestCurrencyRates: ImmutableList<BestCurrencyRate>,
  onBestRateClick: (String) -> Unit,
  onBestRateLongClick: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  useGrid: Boolean = false
) {
  if (useGrid) {
    BestRatesGrid(
      bestCurrencyRates = bestCurrencyRates,
      onBestRateClick = onBestRateClick,
      onBestRateLongClick = onBestRateLongClick,
      modifier = modifier
    )
  } else {
    BestRatesList(
      bestCurrencyRates = bestCurrencyRates,
      onBestRateClick = onBestRateClick,
      onBestRateLongClick = onBestRateLongClick,
      modifier = modifier
    )
  }
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

fun NavGraphBuilder.preferenceScreen(
  navController: NavController,
  useDialog: Boolean = false
) {
  if (useDialog) {
    dialog(DESTINATION_PREFERENCES) {
      PreferenceScreen(
        navController = navController,
        modifier = Modifier.clip(MaterialTheme.shapes.extraLarge),
        preferencesViewModel = koinViewModel()
      )
    }
  } else {
    composable(DESTINATION_PREFERENCES) {
      PreferenceScreen(
        navController = navController,
        preferencesViewModel = koinViewModel()
      )
    }
  }
}

@Composable
private fun PreferenceScreen(
  navController: NavController,
  preferencesViewModel: PreferencesViewModel,
  modifier: Modifier = Modifier
) {

  val defaultCity by preferencesViewModel.defaultCityPreference
    .collectAsStateWithLifecycle(
      initialValue = "Minsk"
    )

  val updateInterval by preferencesViewModel.updateIntervalPreference
    .collectAsStateWithLifecycle(
      initialValue = MIN_UPDATE_INTERVAL_VALUE
    )

  PreferenceScreenContent(
    defaultCity,
    updateInterval,
    preferencesViewModel::updateDefaultCity,
    preferencesViewModel::updateUpdateInterval,
    {
      navController.navigate(DESTINATION_LICENSES)
    },
    modifier
  )
}

fun NavGraphBuilder.licensesScreen() {
  composable(DESTINATION_LICENSES) {
    val openSourceLicensesViewModel: OpenSourceLicensesViewModel = koinViewModel()
    val uriHandler = LocalUriHandler.current

    val licensesState by openSourceLicensesViewModel.licensesState.collectAsStateWithLifecycle()

    OpenSourceLicensesScreen(licensesState = licensesState, onItemClick = { licenseUrl ->
      uriHandler.openUri(licenseUrl)
    })
  }
}

@OptIn(ExperimentalPermissionsApi::class)
fun refreshRates(
  locationPermissionState: PermissionState,
  mainViewModel: MainViewModel
) {
  if (locationPermissionState.status is PermissionStatus.Granted) {
    mainViewModel.forceRefreshExchangeRates()
  } else {
    mainViewModel.forceRefreshExchangeRatesForDefaultCity()
  }
}
