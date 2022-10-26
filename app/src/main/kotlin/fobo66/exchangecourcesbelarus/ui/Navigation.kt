/*
 *    Copyright 2022 Andrey Mukamolov
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.entities.MainScreenState
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesScreen
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesViewModel
import fobo66.exchangecourcesbelarus.ui.main.MainScreen
import fobo66.exchangecourcesbelarus.ui.preferences.MIN_UPDATE_INTERVAL_VALUE
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreen
import fobo66.exchangecourcesbelarus.ui.preferences.PreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val DESTINATION_MAIN = "main"
const val DESTINATION_PREFERENCES = "prefs"
const val DESTINATION_LICENSES = "licenses"

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPermissionsApi::class)
fun NavGraphBuilder.mainScreen(snackbarHostState: SnackbarHostState) {
  composable(DESTINATION_MAIN) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current

    val bestCurrencyRates by mainViewModel.bestCurrencyRates.collectAsStateWithLifecycle(
      initialValue = emptyList()
    )

    val viewState by mainViewModel.screenState.collectAsStateWithLifecycle(
      initialValue = MainScreenState.Loading
    )

    val locationPermissionState = rememberPermissionState(
      permission.ACCESS_COARSE_LOCATION
    )

    var isLocationPermissionPromptShown by rememberSaveable {
      mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
      locationPermissionState.launchPermissionRequest()
    }

    LaunchedEffect(locationPermissionState) {
      if (locationPermissionState.status is PermissionStatus.Granted) {
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
    MainScreen(
      bestCurrencyRates = bestCurrencyRates,
      isRefreshing = viewState.isInProgress,
      onRefresh = {
        if (locationPermissionState.status is PermissionStatus.Granted) {
          mainViewModel.forceRefreshExchangeRates()
        } else {
          mainViewModel.forceRefreshExchangeRatesForDefaultCity()
        }
      },
      onBestRateClick = { bankName ->
        findBankOnMap(mainViewModel, bankName, context, scope, snackbarHostState)
      },
      onBestRateLongClick = { currencyName, currencyValue ->
        mainViewModel.copyCurrencyRateToClipboard(currencyName, currencyValue)
        scope.launch {
          showSnackbar(snackbarHostState, context.getString(string.currency_value_copied))
        }
      }
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

private fun findBankOnMap(
  mainViewModel: MainViewModel,
  bankName: String,
  context: Context,
  scope: CoroutineScope,
  snackbarHostState: SnackbarHostState
) {
  val mapIntent = mainViewModel.findBankOnMap(bankName)

  if (mapIntent != null) {
    startActivity(
      context,
      Intent.createChooser(mapIntent, context.getString(string.open_map)),
      ActivityOptionsCompat.makeBasic().toBundle()
    )
  } else {
    scope.launch {
      showSnackbar(snackbarHostState, context.getString(string.maps_app_required))
    }
  }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
fun NavGraphBuilder.preferenceScreen(navController: NavController) {
  composable(DESTINATION_PREFERENCES) {
    val preferencesViewModel: PreferencesViewModel = hiltViewModel()

    val defaultCity by preferencesViewModel.defaultCityPreference
      .collectAsStateWithLifecycle(
        initialValue = "Minsk"
      )

    val updateInterval by preferencesViewModel.updateIntervalPreference
      .collectAsStateWithLifecycle(
        initialValue = MIN_UPDATE_INTERVAL_VALUE
      )

    PreferenceScreen(
      defaultCity,
      updateInterval,
      preferencesViewModel::updateDefaultCity,
      preferencesViewModel::updateUpdateInterval,
      {
        navController.navigate(DESTINATION_LICENSES)
      }
    )
  }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
fun NavGraphBuilder.licensesScreen() {
  composable(DESTINATION_LICENSES) {
    val openSourceLicensesViewModel: OpenSourceLicensesViewModel = hiltViewModel()
    val context = LocalContext.current

    val licensesState by openSourceLicensesViewModel.licensesState.collectAsStateWithLifecycle()

    OpenSourceLicensesScreen(licensesState = licensesState, onItemClick = { licenseUrl ->
      startActivity(
        context,
        Intent(Intent.ACTION_VIEW, licenseUrl.toUri()),
        ActivityOptionsCompat.makeBasic().toBundle()
      )
    })
  }
}
