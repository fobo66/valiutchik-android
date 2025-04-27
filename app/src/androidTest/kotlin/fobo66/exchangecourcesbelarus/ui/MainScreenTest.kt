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

package fobo66.exchangecourcesbelarus.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.filters.SmallTest
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.main.BestRatesGrid
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@SmallTest
class MainScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyList() {
        composeRule.setContent {
            BestRatesGrid(
                bestCurrencyRates = persistentListOf(),
                onBestRateClick = {},
                onBestRateLongClick = { _, _ -> },
                isRefreshing = false,
                onRefresh = {},
                showExplicitRefresh = true,
                showSettings = true,
                onSettingsClick = {}
            )
        }
        composeRule.onNodeWithTag(TAG_NO_RATES).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun openMap() {
        var isMapOpen = false
        composeRule.setContent {
            BestRatesGrid(
                bestCurrencyRates = persistentListOf(
                    BestCurrencyRate(0, "test", string.app_name, "0.0")
                ),
                onBestRateClick = {
                    isMapOpen = true
                },
                onBestRateLongClick = { _, _ -> },
                isRefreshing = false,
                onRefresh = {},
                showExplicitRefresh = true,
                showSettings = true,
                onSettingsClick = {}
            )
        }
        composeRule.onNodeWithTag(TAG_RATES)
            .onChild()
            .performClick()
        assertTrue(isMapOpen)
    }
}
