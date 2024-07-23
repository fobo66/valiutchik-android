/*
 *    Copyright 2024 Andrey Mukamolov
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

import android.Manifest.permission
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.entities.MainScreenState.Loading
import fobo66.exchangecourcesbelarus.ui.DESTINATION_LICENSES
import fobo66.exchangecourcesbelarus.ui.DESTINATION_MAIN
import fobo66.exchangecourcesbelarus.ui.DESTINATION_PREFERENCES
import fobo66.exchangecourcesbelarus.ui.MainViewModel
import fobo66.exchangecourcesbelarus.ui.TAG_SNACKBAR
import fobo66.exchangecourcesbelarus.ui.TAG_TITLE
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog
import fobo66.exchangecourcesbelarus.ui.bestRatesScreen
import fobo66.exchangecourcesbelarus.ui.licensesScreen
import fobo66.exchangecourcesbelarus.ui.preferenceScreen
import fobo66.exchangecourcesbelarus.ui.refreshRates

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainActivityContent(
  windowSizeClass: WindowSizeClass,
  modifier: Modifier = Modifier,
  mainViewModel: MainViewModel = hiltViewModel()
) {
  val navController = rememberNavController()

  var isAboutDialogShown by remember {
    mutableStateOf(false)
  }

  val locationPermissionState = rememberPermissionState(
    permission.ACCESS_COARSE_LOCATION
  )

  val snackbarHostState = remember {
    SnackbarHostState()
  }

  Scaffold(
    topBar = {
      val currentDestination by navController.currentBackStackEntryAsState()
      val state by mainViewModel.screenState.collectAsState()

      ValiutchikTopBar(
        currentRoute = currentDestination?.destination?.route,
        onBackClick = { navController.popBackStack() },
        onAboutClick = { isAboutDialogShown = true },
        onSettingsClick = { navController.navigate(DESTINATION_PREFERENCES) },
        onRefreshClick = { refreshRates(locationPermissionState, mainViewModel) },
        isRefreshing = state is Loading
      )
    },
    snackbarHost = {
      SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.navigationBarsPadding(),
        snackbar = {
          Snackbar(snackbarData = it, modifier = Modifier.testTag(TAG_SNACKBAR))
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
        mainViewModel = mainViewModel,
        permissionState = locationPermissionState
      )
      preferenceScreen(
        navController,
        useDialog = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
      )
      licensesScreen()
    }
    if (isAboutDialogShown) {
      AboutAppDialog(onDismiss = { isAboutDialogShown = false })
    }
  }
}

private const val TOPBAR_PROGRESS_SCALE = 0.5f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValiutchikTopBar(
  currentRoute: String?,
  onBackClick: () -> Unit,
  onAboutClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onRefreshClick: () -> Unit,
  isRefreshing: Boolean,
  modifier: Modifier = Modifier
) {
  val title = resolveTitle(currentRoute)

  TopAppBar(
    navigationIcon = {
      AnimatedVisibility(currentRoute != DESTINATION_MAIN) {
        IconButton(onClick = onBackClick) {
          Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(string.topbar_description_back)
          )
        }
      }
      AnimatedVisibility(visible = isRefreshing) {
        CircularProgressIndicator(
          modifier = Modifier.scale(TOPBAR_PROGRESS_SCALE)
        )
      }
    },
    title = {
      Text(title, modifier = Modifier.testTag(TAG_TITLE))
    },
    actions = {
      AnimatedVisibility(currentRoute == DESTINATION_MAIN) {
        IconButton(onClick = onRefreshClick) {
          Icon(
            Icons.Default.Refresh,
            contentDescription = stringResource(
              id = string.action_refresh
            )
          )
        }
      }
      IconButton(onClick = onAboutClick) {
        Icon(
          Icons.Default.Info,
          contentDescription = stringResource(
            id = string.action_about
          )
        )
      }
      AnimatedVisibility(currentRoute == DESTINATION_MAIN) {
        IconButton(onClick = onSettingsClick) {
          Icon(
            Icons.Default.Settings,
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
fun resolveTitle(currentRoute: String?): String = when (currentRoute) {
  DESTINATION_PREFERENCES -> stringResource(id = string.title_activity_settings)
  DESTINATION_LICENSES -> stringResource(id = string.title_activity_oss_licenses)
  else -> stringResource(id = string.app_name)
}
