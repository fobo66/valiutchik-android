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

package dev.fobo66.valiutchik.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.filters.SmallTest
import dev.fobo66.valiutchik.ui.rates.BestRatesGrid
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.collections.immutable.persistentListOf

@SmallTest
@OptIn(ExperimentalTestApi::class)
class MainScreenTest {
    @Test
    fun emptyList() = runComposeUiTest {
        setContent {
            BestRatesGrid(
                bestCurrencyRates = persistentListOf(),
                onBestRateClick = {},
                onBestRateLongClick = {},
                isRefreshing = false,
                onRefresh = {},
                showExplicitRefresh = true,
                showSettings = true,
                onSettingsClick = {},
                onShareClick = { _, _ -> }
            )
        }
        onNodeWithTag(TAG_NO_RATES).assertIsDisplayed()
    }

    @Test
    fun openMap() = runComposeUiTest {
        var isMapOpen = false
        setContent {
            BestRatesGrid(
                bestCurrencyRates = persistentListOf(
                    BestCurrencyRate.DollarBuyRate("test", "0.0")
                ),
                onBestRateClick = {
                    isMapOpen = true
                },
                onBestRateLongClick = {},
                isRefreshing = false,
                onRefresh = {},
                showExplicitRefresh = true,
                showSettings = true,
                onSettingsClick = {},
                onShareClick = { _, _ -> }
            )
        }
        onNodeWithTag(TAG_RATES)
            .onChild()
            .performClick()
        assertTrue(isMapOpen)
    }
}
