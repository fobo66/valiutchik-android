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

package dev.fobo66.valiutchik.ui.licenses

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fobo66.valiutchik.presentation.OpenSourceLicensesViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OpenSourceLicensesPanel(
    navigator: ThreePaneScaffoldNavigator<Any>,
    modifier: Modifier = Modifier,
    viewModel: OpenSourceLicensesViewModel = koinViewModel()
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    val licensesState by viewModel.licensesState.collectAsStateWithLifecycle()

    OpenSourceLicensesScreen(
        licensesState = licensesState,
        onItemClick = uriHandler::openUri,
        onBackClick = {
            scope.launch {
                navigator.navigateBack()
            }
        },
        modifier = modifier
    )
}
