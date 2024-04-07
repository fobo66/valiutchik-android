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

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.swipeRight
import androidx.test.filters.MediumTest
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreenContent
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@MediumTest
class PreferencesScreenTest : TestCase(
  kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun showLicenses() = run {
    var showLicense = false
    step("setup screen") {
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
    }
    step("click on licenses") {
      onComposeScreen<SettingsScreen>(composeRule) {
        licensesPreference.performClick()
      }
    }
    step("check if licenses are shown") {
      assertTrue(showLicense)
    }
  }

  @Test
  fun updateIntervalValueChanges() = run {
    var updateInterval = 1f
    step("setup screen") {
      composeRule.setContent {
        PreferenceScreenContent(
          defaultCityValue = "Minsk",
          updateIntervalValue = updateInterval,
          onDefaultCityChange = {},
          onUpdateIntervalChange = { updateInterval = it },
          onOpenSourceLicensesClick = {}
        )
      }
    }
    step("drag update interval slider") {
      onComposeScreen<SettingsScreen>(composeRule) {
        flakySafely {
          updateIntervalPreference.child<KNode> {
            hasTestTag("Slider")
          }.performTouchInput {
            swipeRight()
          }
        }
      }
    }
    step("check value changed") {
      assertNotEquals(1f, updateInterval)
    }
  }

  @Test
  fun defaultCityDialogShown() = run {
    step("setup screen") {
      composeRule.setContent {
        PreferenceScreenContent(
          defaultCityValue = "Minsk",
          updateIntervalValue = 1f,
          onDefaultCityChange = {},
          onUpdateIntervalChange = {},
          onOpenSourceLicensesClick = {}
        )
      }
    }
    step("click on default city") {
      onComposeScreen<SettingsScreen>(composeRule) {
        flakySafely {
          defaultCityPreference.performClick()
        }
      }
    }
    step("check dialog is shown") {
      onComposeScreen<SettingsScreen>(composeRule) {
        flakySafely {
          defaultCityPreferenceDialog.assertIsDisplayed()
        }
      }
    }
  }
}
