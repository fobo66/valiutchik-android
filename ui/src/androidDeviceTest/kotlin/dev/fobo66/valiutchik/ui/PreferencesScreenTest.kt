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
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.filters.SmallTest
import dev.fobo66.valiutchik.ui.preferences.PreferenceScreen
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@SmallTest
@OptIn(ExperimentalTestApi::class)
class PreferencesScreenTest {

    @Test
    fun showLicenses() = runComposeUiTest {
        var showLicense = false
        setContent {
            PreferenceScreen(
                defaultCityValue = "Minsk",
                updateIntervalValue = 1f,
                canOpenSettings = true,
                onDefaultCityChange = {},
                onUpdateIntervalChange = {},
                onOpenSourceLicensesClick = {
                    showLicense = true
                },
                onBackClick = {}
            )
        }

        onNodeWithTag(TAG_LICENSES).performClick()
        assertTrue(showLicense)
    }

    @Test
    fun updateIntervalValueChanges() = runComposeUiTest {
        var updateInterval = 1f

        setContent {
            PreferenceScreen(
                defaultCityValue = "Minsk",
                updateIntervalValue = updateInterval,
                canOpenSettings = true,
                onDefaultCityChange = {},
                onUpdateIntervalChange = { updateInterval = it },
                onOpenSourceLicensesClick = {},
                onBackClick = {}
            )
        }

        onNodeWithTag(TAG_UPDATE_INTERVAL)
            .onChildren()
            .filterToOne(hasTestTag(TAG_SLIDER))
            .performClick()
        assertNotEquals(1f, updateInterval)
    }

    @Test
    fun defaultCityDialogShown() = runComposeUiTest {
        setContent {
            PreferenceScreen(
                defaultCityValue = "Minsk",
                updateIntervalValue = 1f,
                canOpenSettings = true,
                onDefaultCityChange = {},
                onUpdateIntervalChange = {},
                onOpenSourceLicensesClick = {},
                onBackClick = {}
            )
        }

        onNodeWithTag(TAG_DEFAULT_CITY).performClick()
        onNode(isDialog()).assertIsDisplayed()
    }
}
