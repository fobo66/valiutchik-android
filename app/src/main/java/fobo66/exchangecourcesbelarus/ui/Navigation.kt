package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import fobo66.exchangecourcesbelarus.entities.Licenses
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
  SmallTopAppBar(
    title = {
      Text(text = stringResource(id = string.app_name))
    },
    actions = {
      IconButton(onClick = onAboutClick) {
        Icon(
          Info,
          contentDescription = stringResource(
            id = string.action_about
          )
        )
      }
      IconButton(onClick = onSettingsClicked) {
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

    val isRefreshing by mainViewModel.progress.collectAsStateWithLifecycle(
      initialValue = false
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
        MainScreen(
          bestCurrencyRates = bestCurrencyRates,
          isRefreshing = isRefreshing,
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

    val licenses by openSourceLicensesViewModel.licenses.collectAsStateWithLifecycle(
      initialValue = Licenses(emptyList())
    )

    OpenSourceLicensesScreen(licenses = licenses, onItemClick = { licenseUrl ->
      startActivity(
        context,
        Intent(Intent.ACTION_VIEW, licenseUrl.toUri()),
        ActivityOptionsCompat.makeBasic().toBundle()
      )
    })
  }
}
