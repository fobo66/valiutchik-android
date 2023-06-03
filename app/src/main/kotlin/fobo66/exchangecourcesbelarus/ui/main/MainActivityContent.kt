/*
 *    Copyright 2023 Andrey Mukamolov
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

package fobo66.exchangecourcesbelarus.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.DESTINATION_LICENSES
import fobo66.exchangecourcesbelarus.ui.DESTINATION_MAIN
import fobo66.exchangecourcesbelarus.ui.DESTINATION_PREFERENCES
import fobo66.exchangecourcesbelarus.ui.MainViewModel
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog
import fobo66.exchangecourcesbelarus.ui.bestRatesScreen
import fobo66.exchangecourcesbelarus.ui.icons.Info
import fobo66.exchangecourcesbelarus.ui.icons.Settings
import fobo66.exchangecourcesbelarus.ui.licensesScreen
import fobo66.exchangecourcesbelarus.ui.preferenceScreen
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@Composable
fun MainActivityContent(windowSizeClass: WindowSizeClass, modifier: Modifier = Modifier) {
  val navController = rememberNavController()

  var isAboutDialogShown by remember {
    mutableStateOf(false)
  }

  val snackbarHostState = remember {
    SnackbarHostState()
  }

  val mainViewModel: MainViewModel = hiltViewModel()

  ValiutchikTheme {
    Scaffold(
      topBar = {
        val currentDestination by navController.currentBackStackEntryAsState()

        ValiutchikTopBar(
          currentRoute = currentDestination?.destination?.route,
          onBackClick = {
            navController.popBackStack()
          },
          onAboutClick = {
            isAboutDialogShown = true
          },
          onSettingsClicked = {
            navController.navigate(DESTINATION_PREFERENCES)
          },
          onRefreshClicked = {
            mainViewModel.refreshExchangeRates()
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
      },
      modifier = modifier
    ) {
      NavHost(
        navController = navController,
        startDestination = DESTINATION_MAIN,
        modifier = Modifier.padding(it)
      ) {
        bestRatesScreen(
          snackbarHostState,
          useGrid = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact,
          mainViewModel = mainViewModel
        )
        preferenceScreen(
          navController,
          useDialog = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        )
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
  currentRoute: String?,
  onBackClick: () -> Unit,
  onAboutClick: () -> Unit,
  onSettingsClicked: () -> Unit,
  onRefreshClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  val title = resolveTitle(currentRoute)

  TopAppBar(
    navigationIcon = {
      AnimatedVisibility(currentRoute != DESTINATION_MAIN) {
        IconButton(onClick = onBackClick, modifier = Modifier.testTag("Back")) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
      }
    },
    title = {
      Text(title, modifier = Modifier.testTag("Title"))
    },
    actions = {
      AnimatedVisibility(currentRoute == DESTINATION_MAIN) {
        IconButton(onClick = onRefreshClicked, modifier = Modifier.testTag("Refresh")) {
          Icon(
            Icons.Default.Refresh,
            contentDescription = stringResource(
              id = string.action_refresh
            )
          )
        }
      }
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
    },
    modifier = modifier
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

  SideEffect {
    systemUiController.setSystemBarsColor(
      color = Color.Transparent,
      darkIcons = useDarkIcons,
      isNavigationBarContrastEnforced = false
    )
  }
}
