package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@AndroidEntryPoint
class NewMainActivity : ComponentActivity() {
  @OptIn(
    ExperimentalMaterial3Api::class
  )
  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.ExchangeCoursesTheme)
    super.onCreate(savedInstanceState)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val navController = rememberNavController()

      var isAboutDialogShown by remember {
        mutableStateOf(false)
      }

      val snackbarHostState = remember {
        SnackbarHostState()
      }

      ValiutchikTheme {
        Scaffold(
          topBar = {
            ValiutchikTopBar(
              onAboutClick = {
                isAboutDialogShown = true
              },
              onSettingsClicked = {
                navController.navigate(DESTINATION_PREFERENCES)
              }
            )
          },
          snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.navigationBarsPadding())
          }
        ) {
          NavHost(
            navController = navController,
            startDestination = DESTINATION_MAIN,
            modifier = Modifier.padding(it)
          ) {
            mainScreen(snackbarHostState)
            preferenceScreen(navController)
            licensesScreen()
          }
          if (isAboutDialogShown) {
            AboutAppDialog(
              onDismiss = { isAboutDialogShown = false }
            )
          }
        }
      }
    }
  }
}
