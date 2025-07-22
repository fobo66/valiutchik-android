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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.fobo66.valiutchik.ui.TAG_TITLE
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopBar(
    title: String,
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRefreshClick: () -> Unit,
    showRefresh: Boolean,
    modifier: Modifier = Modifier,
    settingsVisible: Boolean = true
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(text = title, modifier = Modifier.testTag(TAG_TITLE))
        },
        actions = {
            AppBarRow(
                overflowIndicator = {
                    IconButton(onClick = { it.show() }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(R.string.toolbar_action_more)
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = onAboutClick,
                    icon = {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = context.getString(R.string.action_about)
                        )
                    },
                    label = context.getString(R.string.action_about)
                )
                if (showRefresh) {
                    clickableItem(
                        onClick = onRefreshClick,
                        icon = {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = context.getString(
                                    R.string.widget_action_refresh
                                )
                            )
                        },
                        label = context.getString(R.string.widget_action_refresh)
                    )
                }
                if (settingsVisible) {
                    clickableItem(
                        onClick = onSettingsClick,
                        icon = {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = context.getString(R.string.action_settings)
                            )
                        },
                        label = context.getString(R.string.action_settings)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(title: String, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.topbar_description_back)
                )
            }
        },
        title = {
            Text(
                text = title,
                modifier = Modifier.testTag(TAG_TITLE)
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun PrimaryTopbarPreview() {
    ValiutchikTheme {
        PrimaryTopBar(
            title = "Test",
            showRefresh = true,
            onSettingsClick = {},
            onAboutClick = {},
            onRefreshClick = {}
        )
    }
}

@Preview
@Composable
private fun TertiaryTopbarPreview() {
    ValiutchikTheme {
        SecondaryTopBar(
            title = "Test",
            onBackClick = {}
        )
    }
}
