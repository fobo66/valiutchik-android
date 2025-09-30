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

package dev.fobo66.valiutchik.ui.element

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.fobo66.valiutchik.ui.TAG_TITLE
import dev.fobo66.valiutchik.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import valiutchik.ui.generated.resources.Res
import valiutchik.ui.generated.resources.action_about
import valiutchik.ui.generated.resources.action_refresh
import valiutchik.ui.generated.resources.action_settings
import valiutchik.ui.generated.resources.ic_arrow_back
import valiutchik.ui.generated.resources.ic_info
import valiutchik.ui.generated.resources.ic_refresh
import valiutchik.ui.generated.resources.ic_settings
import valiutchik.ui.generated.resources.toolbar_action_more
import valiutchik.ui.generated.resources.topbar_description_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopBar(
    title: String,
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRefreshClick: () -> Unit,
    showRefresh: Boolean,
    modifier: Modifier = Modifier,
    settingsVisible: Boolean = true,
    aboutLabel: String = stringResource(Res.string.action_about),
    refreshLabel: String = stringResource(Res.string.action_refresh),
    settingsLabel: String = stringResource(Res.string.action_settings)
) {
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
                            contentDescription = stringResource(Res.string.toolbar_action_more)
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = onAboutClick,
                    icon = {
                        Icon(
                            painterResource(Res.drawable.ic_info),
                            contentDescription = aboutLabel
                        )
                    },
                    label = aboutLabel
                )
                if (showRefresh) {
                    clickableItem(
                        onClick = onRefreshClick,
                        icon = {
                            Icon(
                                painterResource(Res.drawable.ic_refresh),
                                contentDescription = refreshLabel
                            )
                        },
                        label = refreshLabel
                    )
                }
                if (settingsVisible) {
                    clickableItem(
                        onClick = onSettingsClick,
                        icon = {
                            Icon(
                                painterResource(Res.drawable.ic_settings),
                                contentDescription = settingsLabel
                            )
                        },
                        label = settingsLabel
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
                    painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = stringResource(Res.string.topbar_description_back)
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
    AppTheme {
        PrimaryTopBar(
            title = "Test",
            onAboutClick = {},
            onSettingsClick = {},
            onRefreshClick = {},
            showRefresh = true,
            settingsLabel = stringResource(Res.string.action_settings)
        )
    }
}

@Preview
@Composable
private fun TertiaryTopbarPreview() {
    AppTheme {
        SecondaryTopBar(
            title = "Test",
            onBackClick = {}
        )
    }
}
