package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.google.accompanist.permissions.shouldShowRationale
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesScreen
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesViewModel
import fobo66.exchangecourcesbelarus.ui.main.MainScreen
import fobo66.exchangecourcesbelarus.ui.main.MainScreenNoPermission
import fobo66.exchangecourcesbelarus.ui.preferences.MIN_UPDATE_INTERVAL_VALUE
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreen
import fobo66.exchangecourcesbelarus.ui.preferences.PreferencesViewModel

const val DESTINATION_MAIN = "main"
const val DESTINATION_PREFERENCES = "prefs"
const val DESTINATION_LICENSES = "licenses"

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPermissionsApi::class)
fun NavGraphBuilder.mainScreen() {
  composable(DESTINATION_MAIN) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val bestCurrencyRates by mainViewModel.bestCurrencyRates.collectAsStateWithLifecycle(
      initialValue = emptyList()
    )

    val isRefreshing by mainViewModel.progress.collectAsStateWithLifecycle(
      initialValue = false
    )

    val locationPermissionState = rememberPermissionState(
      permission.ACCESS_COARSE_LOCATION
    )

    when (locationPermissionState.status) {
      is PermissionStatus.Granted -> {
        LaunchedEffect(locationPermissionState) {
          mainViewModel.refreshExchangeRates()
        }
        MainScreen(
          bestCurrencyRates = bestCurrencyRates,
          isRefreshing = isRefreshing,
          onRefresh = { mainViewModel.refreshExchangeRates() },
          onBestRateClick = { /*TODO*/ },
          onBestRateLongClick = { /*TODO*/ }
        )
      }
      is PermissionStatus.Denied -> {
        val textToShow = stringResource(
          id = if (locationPermissionState.status.shouldShowRationale) {
            string.permission_description_rationale
          } else {
            string.permission_description
          }
        )
        MainScreenNoPermission(
          textToShow = textToShow,
          onRequestPermission = { locationPermissionState.launchPermissionRequest() },
          modifier = Modifier.fillMaxSize()
        )
      }
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

    val licenses by openSourceLicensesViewModel.licenses.collectAsStateWithLifecycle(
      initialValue = emptyList()
    )

    OpenSourceLicensesScreen(licenses = licenses, onItemClick = { licenseUrl ->
      startActivity(context, Intent(Intent.ACTION_VIEW, licenseUrl.toUri()), null)
    })
  }
}
