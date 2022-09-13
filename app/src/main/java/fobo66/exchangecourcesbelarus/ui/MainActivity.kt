package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @OptIn(
    ExperimentalMaterial3Api::class
  )
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val navController = rememberNavController()

      SystemBarColors()

      var isAboutDialogShown by remember {
        mutableStateOf(false)
      }

      val snackbarHostState = remember {
        SnackbarHostState()
      }

      ValiutchikTheme {
        Scaffold(
          topBar = {
            val currentDestination by navController.currentBackStackEntryAsState()

            ValiutchikTopBar(
              currentPosition = currentDestination,
              onBackClick = {
                navController.popBackStack()
              },
              onAboutClick = {
                isAboutDialogShown = true
              },
              onSettingsClicked = {
                navController.navigate(DESTINATION_PREFERENCES)
              }
            )
          },
          snackbarHost = {
            SnackbarHost(
              hostState = snackbarHostState,
              modifier = Modifier.navigationBarsPadding(),
              snackbar = {
                Snackbar(snackbarData = it, modifier = Modifier.testTag("Snackbar"))
              }
            )
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

  @Composable
  private fun SystemBarColors() {
    val systemUiController = rememberSystemUiController()

    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
      systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = useDarkIcons
      )
      onDispose {}
    }
  }
}
