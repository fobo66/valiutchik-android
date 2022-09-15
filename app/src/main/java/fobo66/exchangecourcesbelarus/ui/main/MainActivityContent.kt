package fobo66.exchangecourcesbelarus.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.DESTINATION_LICENSES
import fobo66.exchangecourcesbelarus.ui.DESTINATION_MAIN
import fobo66.exchangecourcesbelarus.ui.DESTINATION_PREFERENCES
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog
import fobo66.exchangecourcesbelarus.ui.icons.Info
import fobo66.exchangecourcesbelarus.ui.icons.Settings
import fobo66.exchangecourcesbelarus.ui.licensesScreen
import fobo66.exchangecourcesbelarus.ui.mainScreen
import fobo66.exchangecourcesbelarus.ui.preferenceScreen
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent() {
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
        val currentRoute by remember(currentDestination) {
          derivedStateOf {
            currentDestination?.destination?.route
          }
        }
        val title = resolveTitle(currentRoute)

        ValiutchikTopBar(
          title = title,
          currentRoute = currentRoute,
          onBackClick = {
            navController.popBackStack()
          },
          onAboutClick = {
            isAboutDialogShown = true
          }
        ) {
          navController.navigate(DESTINATION_PREFERENCES)
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValiutchikTopBar(
  title: String,
  currentRoute: String?,
  onBackClick: () -> Unit,
  onAboutClick: () -> Unit,
  onSettingsClicked: () -> Unit
) {
  TopAppBar(
    navigationIcon = {
      AnimatedVisibility(currentRoute != DESTINATION_MAIN) {
        IconButton(onClick = onBackClick) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
      }
    },
    title = {
      Text(title)
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
      AnimatedVisibility(currentRoute == DESTINATION_MAIN) {
        IconButton(onClick = onSettingsClicked, modifier = Modifier.testTag("Settings")) {
          Icon(
            Settings,
            contentDescription = stringResource(
              id = string.action_settings
            )
          )
        }
      }
    }
  )
}

@Composable
fun resolveTitle(currentRoute: String?): String {
  return when (currentRoute) {
    DESTINATION_PREFERENCES -> stringResource(id = string.title_activity_settings)
    DESTINATION_LICENSES -> stringResource(id = string.title_activity_oss_licenses)
    else -> stringResource(id = string.app_name)
  }
}

@Composable
fun SystemBarColors() {
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
