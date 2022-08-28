package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@AndroidEntryPoint
class NewMainActivity : ComponentActivity() {
  @OptIn(
    ExperimentalMaterial3Api::class
  )
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val navController = rememberNavController()

      // Remember a SystemUiController
      val systemUiController = rememberSystemUiController()
      val useDarkIcons = !isSystemInDarkTheme()

      DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons
        )
        onDispose {}
      }

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
