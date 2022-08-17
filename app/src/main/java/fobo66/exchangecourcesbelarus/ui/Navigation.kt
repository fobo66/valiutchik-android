package fobo66.exchangecourcesbelarus.ui

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import fobo66.exchangecourcesbelarus.ui.preferences.MIN_UPDATE_INTERVAL_VALUE
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreen
import fobo66.exchangecourcesbelarus.ui.preferences.PreferencesViewModel

const val DESTINATION_PREFERENCES = "prefs"

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
        navController.navigate("licenses")
      }
    )
  }
}
