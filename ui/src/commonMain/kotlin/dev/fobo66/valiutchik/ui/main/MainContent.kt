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

package dev.fobo66.valiutchik.ui.main

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import dev.fobo66.valiutchik.ui.TAG_SNACKBAR
import dev.fobo66.valiutchik.ui.element.PanelsBackHandler
import dev.fobo66.valiutchik.ui.licenses.OpenSourceLicensesPanel
import dev.fobo66.valiutchik.ui.preferences.PreferencesPanel
import dev.fobo66.valiutchik.ui.rates.RatesPanel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    showManualRefresh: Boolean = false,
    canOpenSettings: Boolean = true
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
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
        MainScreenPanels(
            snackbarHostState = snackbarHostState,
            manualRefreshVisible = showManualRefresh,
            canOpenSettings = canOpenSettings,
            modifier =
            Modifier
                .padding(
                    start = it.calculateStartPadding(layoutDirection),
                    end = it.calculateEndPadding(layoutDirection),
                    top = it.calculateTopPadding()
                )
                .consumeWindowInsets(it)
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreenPanels(
    snackbarHostState: SnackbarHostState,
    manualRefreshVisible: Boolean,
    canOpenSettings: Boolean,
    modifier: Modifier = Modifier
) {
    val navigator = rememberSupportingPaneScaffoldNavigator(
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(
            supportingPaneAdaptStrategy = AdaptStrategy.Hide
        )
    )

    PanelsBackHandler(navigator)

    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        mainPane = {
            AnimatedPane(modifier = Modifier.safeContentPadding()) {
                RatesPanel(
                    snackbarHostState = snackbarHostState,
                    manualRefreshVisible = manualRefreshVisible,
                    canOpenSettings = canOpenSettings,
                    onOpenSettings = {
                        navigator.navigateTo(ThreePaneScaffoldRole.Secondary)
                    }
                )
            }
        },
        supportingPane = {
            AnimatedPane(modifier = Modifier.safeContentPadding()) {
                PreferencesPanel(
                    canOpenSettings = canOpenSettings,
                    onBack = { navigator.navigateBack() },
                    onOpenLicenses = {
                        navigator.navigateTo(
                            ThreePaneScaffoldRole.Tertiary
                        )
                    }
                )
            }
        },
        extraPane = {
            AnimatedPane(modifier = Modifier.safeContentPadding()) {
                OpenSourceLicensesPanel(onBack = { navigator.navigateBack() })
            }
        },
        paneExpansionState = rememberPaneExpansionState(
            keyProvider = navigator.scaffoldValue
        ),
        paneExpansionDragHandle = { state ->
            val interactionSource = remember { MutableInteractionSource() }
            VerticalDragHandle(
                modifier =
                Modifier.paneExpansionDraggable(
                    state,
                    LocalMinimumInteractiveComponentSize.current,
                    interactionSource
                ),
                interactionSource = interactionSource
            )
        },
        modifier = modifier
    )
}
