package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
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
import com.google.accompanist.permissions.shouldShowRationale
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.entities.LicensesState
import fobo66.exchangecourcesbelarus.entities.MainScreenState
import fobo66.exchangecourcesbelarus.ui.icons.Info
import fobo66.exchangecourcesbelarus.ui.icons.Settings
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesScreen
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesViewModel
import fobo66.exchangecourcesbelarus.ui.main.MainScreen
import fobo66.exchangecourcesbelarus.ui.main.MainScreenNoPermission
import fobo66.exchangecourcesbelarus.ui.preferences.MIN_UPDATE_INTERVAL_VALUE
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreen
import fobo66.exchangecourcesbelarus.ui.preferences.PreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val DESTINATION_MAIN = "main"
const val DESTINATION_PREFERENCES = "prefs"
const val DESTINATION_LICENSES = "licenses"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValiutchikTopBar(onAboutClick: () -> Unit, onSettingsClicked: () -> Unit) {
  TopAppBar(
    title = {
      Text(text = stringResource(id = string.app_name))
    },
    actions = {
      IconButton(onClick = onAboutClick, modifier = Modifier.testTag("About")) {
        Icon(
          Info,
          contentDescription = stringResource(
            id = string.action_about
          )
        )
      }
      IconButton(onClick = onSettingsClicked, modifier = Modifier.testTag("Settings")) {
        Icon(
          Settings,
          contentDescription = stringResource(
            id = string.action_settings
          )
        )
      }
    }
  )
}

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPermissionsApi::class)
fun NavGraphBuilder.mainScreen(snackbarHostState: SnackbarHostState) {
  composable(DESTINATION_MAIN) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current

    val bestCurrencyRates by mainViewModel.bestCurrencyRates.collectAsStateWithLifecycle(
      initialValue = emptyList()
    )

    val viewState by mainViewModel.mainScreenState.collectAsStateWithLifecycle(
      initialValue = MainScreenState.Loading
    )

    val locationPermissionState = rememberPermissionState(
      permission.ACCESS_COARSE_LOCATION
    )

    val scope = rememberCoroutineScope()

    when (locationPermissionState.status) {
      is PermissionStatus.Granted -> {
        LaunchedEffect(locationPermissionState) {
          mainViewModel.refreshExchangeRates()
        }
        LaunchedEffect(viewState) {
          if (viewState is MainScreenState.Error) {
            snackbarHostState.showSnackbar(
              message = context.getString(string.get_data_error),
              duration = Short
            )
          }
        }
        MainScreen(
          bestCurrencyRates = bestCurrencyRates,
          isRefreshing = viewState.isInProgress,
          onRefresh = { mainViewModel.refreshExchangeRates() },
          onBestRateClick = { bankName ->
            findBankOnMap(mainViewModel, bankName, context, scope, snackbarHostState)
          },
          onBestRateLongClick = { currencyName, currencyValue ->
            mainViewModel.copyCurrencyRateToClipboard(currencyName, currencyValue)
            scope.launch {
              snackbarHostState.showSnackbar(
                message = context.getString(string.currency_value_copied),
                duration = Short
              )
            }
          }
        )
      }
      is PermissionStatus.Denied -> {
        MainScreenNoPermission(
          shouldShowRationale = locationPermissionState.status.shouldShowRationale,
          onRequestPermission = { locationPermissionState.launchPermissionRequest() },
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
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
      snackbarHostState.showSnackbar(
        message = context.getString(string.maps_app_required),
        duration = Short
      )
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

    val licensesState by openSourceLicensesViewModel.licensesState.collectAsStateWithLifecycle(
      initialValue = LicensesState(emptyList())
    )

    OpenSourceLicensesScreen(licensesState = licensesState, onItemClick = { licenseUrl ->
      startActivity(
        context,
        Intent(Intent.ACTION_VIEW, licenseUrl.toUri()),
        ActivityOptionsCompat.makeBasic().toBundle()
      )
    })
  }
}
