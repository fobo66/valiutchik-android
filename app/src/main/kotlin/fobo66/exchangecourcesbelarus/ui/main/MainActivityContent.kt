/*
 *    Copyright 2025 Andrey Mukamolov
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
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import fobo66.exchangecourcesbelarus.ui.BestRatesScreenDestination
import fobo66.exchangecourcesbelarus.ui.OpenSourceLicensesDestination
import fobo66.exchangecourcesbelarus.ui.PreferenceScreenDestination
import fobo66.exchangecourcesbelarus.ui.TAG_SNACKBAR
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainActivityContent(
  windowSizeClass: WindowSizeClass,
  modifier: Modifier = Modifier,
) {
  val snackbarHostState = remember { SnackbarHostState() }

  Scaffold(
    snackbarHost = {
      SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.navigationBarsPadding(),
        snackbar = {
          Snackbar(snackbarData = it, modifier = Modifier.testTag(TAG_SNACKBAR))
        },
      )
    },
    modifier = modifier,
  ) {
    val layoutDirection = LocalLayoutDirection.current
    MainScreenPanels(
      snackbarHostState = snackbarHostState,
      manualRefreshVisible = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact,
      canOpenSettings = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded,
      modifier =
        Modifier.padding(
          start = it.calculateStartPadding(layoutDirection),
          end = it.calculateEndPadding(layoutDirection),
          top = it.calculateTopPadding(),
        )
          .consumeWindowInsets(it),
    )
  }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreenPanels(
  snackbarHostState: SnackbarHostState,
  manualRefreshVisible: Boolean,
  canOpenSettings: Boolean,
  modifier: Modifier = Modifier,
) {
  val navigator = rememberSupportingPaneScaffoldNavigator()
  val locationPermissionState = rememberPermissionState(permission.ACCESS_COARSE_LOCATION)
  val scope = rememberCoroutineScope()

  BackHandler(navigator.canNavigateBack()) { scope.launch { navigator.navigateBack() } }

  SupportingPaneScaffold(
    directive = navigator.scaffoldDirective,
    value = navigator.scaffoldValue,
    mainPane = {
      AnimatedPane {
        BestRatesScreenDestination(
          navigator = navigator,
          snackbarHostState = snackbarHostState,
          manualRefreshVisible = manualRefreshVisible,
          canOpenSettings = canOpenSettings,
          permissionState = locationPermissionState,
        )
      }
    },
    supportingPane = {
      AnimatedPane {
        PreferenceScreenDestination(
          navigator = navigator,
          canOpenSettings = canOpenSettings
        )
      }
    },
    extraPane = {
      AnimatedPane {
        OpenSourceLicensesDestination(navigator = navigator)
      }
    },
    modifier = modifier,
  )
}
