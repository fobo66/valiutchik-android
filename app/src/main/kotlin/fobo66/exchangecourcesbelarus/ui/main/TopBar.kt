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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.TAG_TITLE
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValiutchikTopBar(
  currentScreen: ThreePaneScaffoldRole?,
  onBackClick: () -> Unit,
  onAboutClick: () -> Unit,
  onSettingsClick: () -> Unit,
  modifier: Modifier = Modifier,
  updateTitle: Boolean = true,
  settingsVisible: Boolean = true,
) {
  TopAppBar(
    navigationIcon = {
      AnimatedVisibility(currentScreen != ThreePaneScaffoldRole.Primary) {
        IconButton(onClick = onBackClick) {
          Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(R.string.topbar_description_back),
          )
        }
      }
    },
    title = {
      Text(
        text =
          if (updateTitle) {
            resolveTitle(currentScreen)
          } else {
            stringResource(id = R.string.app_name)
          },
        modifier = Modifier.testTag(TAG_TITLE),
      )
    },
    actions = {
      IconButton(onClick = onAboutClick) {
        Icon(
          Icons.Default.Info,
          contentDescription =
            stringResource(
              id = R.string.action_about,
            ),
        )
      }
      AnimatedVisibility(currentScreen == ThreePaneScaffoldRole.Primary && settingsVisible) {
        IconButton(onClick = onSettingsClick) {
          Icon(
            Icons.Default.Settings,
            contentDescription =
              stringResource(
                id = R.string.action_settings,
              ),
          )
        }
      }
    },
    modifier = modifier,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelTopBar(
  title: String,
  onBackClick: () -> Unit,
  onAboutClick: () -> Unit,
  onSettingsClick: () -> Unit,
  modifier: Modifier = Modifier,
  settingsVisible: Boolean = true,
  showBack: Boolean = true,
) {
  TopAppBar(
    navigationIcon = {
      AnimatedVisibility(showBack) {
        IconButton(onClick = onBackClick) {
          Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(R.string.topbar_description_back),
          )
        }
      }
    },
    title = {
      Text(
        text = title,
        modifier = Modifier.testTag(TAG_TITLE),
      )
    },
    actions = {
      IconButton(onClick = onAboutClick) {
        Icon(
          Icons.Default.Info,
          contentDescription =
            stringResource(
              id = R.string.action_about,
            ),
        )
      }
      AnimatedVisibility(settingsVisible) {
        IconButton(onClick = onSettingsClick) {
          Icon(
            Icons.Default.Settings,
            contentDescription =
              stringResource(
                id = R.string.action_settings,
              ),
          )
        }
      }
    },
    modifier = modifier,
  )
}

@Preview
@Composable
private fun PanelTopbarPreview() {
  ValiutchikTheme {
    PanelTopBar(
      title = "Test",
      onBackClick = {},
      onSettingsClick = {},
      onAboutClick = {}
    )
  }
}
