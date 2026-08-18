/*
 *    Copyright 2026 Andrey Mukamolov
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

package dev.fobo66.valiutchik.ui.rates

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.fobo66.valiutchik.ui.share.rememberShareProvider
import dev.zacsweers.metro.AppScope

@CircuitInject(RatesScreen::class, AppScope::class)
@Composable
fun RatesScreen(state: RatesScreen.State, modifier: Modifier) {
    val shareProvider = rememberShareProvider()

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val showSettings =
        !windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    val showExplicitRefresh =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    BestRatesGrid(
        bestCurrencyRates = state.rates,
        onBestRateClick = { bankName ->
            state.eventSink(RatesScreenEvent.OpenOnMap(bankName))
        },
        onBestRateLongClick = { currencyValue ->
            state.eventSink(RatesScreenEvent.CopyToClipboard(currencyValue))
        },
        onShareClick = { currencyName, currencyValue ->
            shareProvider.shareText(currencyName, currencyValue)
        },
        showExplicitRefresh = showExplicitRefresh,
        showSettings = showSettings,
        onSettingsClick = {
            state.eventSink(RatesScreenEvent.OpenSettings)
        },
        isRefreshing = state.isLoading,
        onRefresh = {
            state.eventSink(RatesScreenEvent.Refresh)
        },
        modifier = modifier
    )
}
