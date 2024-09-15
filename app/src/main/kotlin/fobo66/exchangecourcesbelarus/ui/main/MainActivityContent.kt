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
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.BestRatesScreenDestination
import fobo66.exchangecourcesbelarus.ui.OpenSourceLicensesDestination
import fobo66.exchangecourcesbelarus.ui.PreferenceScreen
import fobo66.exchangecourcesbelarus.ui.TAG_SNACKBAR
import fobo66.exchangecourcesbelarus.ui.TAG_TITLE
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainActivityContent(
  windowSizeClass: WindowSizeClass,
  modifier: Modifier = Modifier
) {
  val navigator = rememberSupportingPaneScaffoldNavigator()

  BackHandler(enabled = navigator.canNavigateBack()) {
    navigator.navigateBack()
  }

  var isAboutDialogShown by remember { mutableStateOf(false) }

  val locationPermissionState = rememberPermissionState(permission.ACCESS_COARSE_LOCATION)

  val snackbarHostState = remember { SnackbarHostState() }

  Scaffold(
    topBar = {
      ValiutchikTopBar(
        currentScreen = navigator.currentDestination?.pane,
        onBackClick = { navigator.navigateBack() },
        onAboutClick = { isAboutDialogShown = true },
        onSettingsClick = { navigator.navigateTo(ThreePaneScaffoldRole.Secondary) },
        updateTitle = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        settingsVisible = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
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
    val layoutDirection = LocalLayoutDirection.current
    SupportingPaneScaffold(
      directive = navigator.scaffoldDirective,
      value = navigator.scaffoldValue,
      mainPane = {
        BestRatesScreenDestination(
          snackbarHostState = snackbarHostState,
          permissionState = locationPermissionState,
          useGrid = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        )
      },
      supportingPane = {
        PreferenceScreen(
          onLicensesClick = { navigator.navigateTo(ThreePaneScaffoldRole.Tertiary) }
        )
      },
      extraPane = {
        OpenSourceLicensesDestination()
      },
      modifier = Modifier.padding(
        start = it.calculateStartPadding(layoutDirection),
        end = it.calculateEndPadding(layoutDirection),
        top = it.calculateTopPadding()
      )
    )
    if (isAboutDialogShown) {
      AboutAppDialog(onDismiss = { isAboutDialogShown = false })
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValiutchikTopBar(
  currentScreen: ThreePaneScaffoldRole?,
  onBackClick: () -> Unit,
  onAboutClick: () -> Unit,
  onSettingsClick: () -> Unit,
  modifier: Modifier = Modifier,
  updateTitle: Boolean = true,
  settingsVisible: Boolean = true
) {

  TopAppBar(
    navigationIcon = {
      AnimatedVisibility(currentScreen != ThreePaneScaffoldRole.Primary) {
        IconButton(onClick = onBackClick) {
          Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(string.topbar_description_back)
          )
        }
      }
    },
    title = {
      Text(
        text = if (updateTitle) {
          resolveTitle(currentScreen)
        } else {
          stringResource(id = string.app_name)
        }, modifier = Modifier.testTag(TAG_TITLE)
      )
    },
    actions = {
      IconButton(onClick = onAboutClick) {
        Icon(
          Icons.Default.Info,
          contentDescription = stringResource(
            id = string.action_about
          )
        )
      }
      AnimatedVisibility(currentScreen == ThreePaneScaffoldRole.Primary && settingsVisible) {
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
fun resolveTitle(currentScreen: ThreePaneScaffoldRole?): String = when (currentScreen) {
  ThreePaneScaffoldRole.Secondary -> stringResource(id = string.title_activity_settings)
  ThreePaneScaffoldRole.Tertiary -> stringResource(id = string.title_activity_oss_licenses)
  else -> stringResource(id = string.app_name)
}
