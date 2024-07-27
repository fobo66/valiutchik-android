/*
 *    Copyright 2024 Andrey Mukamolov
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
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.filters.SmallTest
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreenContent
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@SmallTest
class PreferencesScreenTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun showLicenses() {
    var showLicense = false
    composeRule.setContent {
      PreferenceScreenContent(
        defaultCityValue = "Minsk",
        updateIntervalValue = 1f,
        onDefaultCityChange = {},
        onUpdateIntervalChange = {},
        onOpenSourceLicensesClick = {
          showLicense = true
        }
      )
    }

    composeRule.onNodeWithTag(TAG_LICENSES).performClick()
    assertTrue(showLicense)
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun updateIntervalValueChanges() {
    var updateInterval = 1f

    composeRule.setContent {
      PreferenceScreenContent(
        defaultCityValue = "Minsk",
        updateIntervalValue = updateInterval,
        onDefaultCityChange = {},
        onUpdateIntervalChange = { updateInterval = it },
        onOpenSourceLicensesClick = {}
      )
    }

    composeRule.onNodeWithTag(TAG_UPDATE_INTERVAL)
      .onChildren()
      .filterToOne(hasTestTag(TAG_SLIDER))
      .performClick()
    assertNotEquals(1f, updateInterval)
  }

  @Test
  fun defaultCityDialogShown() {

    composeRule.setContent {
      PreferenceScreenContent(
        defaultCityValue = "Minsk",
        updateIntervalValue = 1f,
        onDefaultCityChange = {},
        onUpdateIntervalChange = {},
        onOpenSourceLicensesClick = {}
      )
    }

    composeRule.onNodeWithTag(TAG_DEFAULT_CITY).performClick()
    composeRule.onNode(isDialog()).assertIsDisplayed()
  }
}
