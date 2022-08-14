package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesScreen
import fobo66.exchangecourcesbelarus.ui.preferences.MIN_UPDATE_INTERVAL_VALUE
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreen
import fobo66.exchangecourcesbelarus.ui.preferences.PreferencesViewModel
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@AndroidEntryPoint
class NewMainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()

      ValiutchikTheme {
        Scaffold(topBar = {
          SmallTopAppBar(title = {
            Text(text = stringResource(id = string.app_name))
          })
        }) {
          NavHost(
            navController = navController,
            startDestination = "prefs",
            modifier = Modifier.padding(it)
          ) {
            composable("prefs") {
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
            composable("licenses") {
              OpenSourceLicensesScreen()
            }
          }
        }
      }
    }
  }
}
