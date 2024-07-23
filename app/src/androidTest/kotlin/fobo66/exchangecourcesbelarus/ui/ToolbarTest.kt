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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.main.ValiutchikTopBar
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@MediumTest
class ToolbarTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun showCorrectTitle() {
    composeRule.setContent {
      ValiutchikTopBar(
        currentRoute = DESTINATION_MAIN,
        onBackClick = {},
        onAboutClick = {},
        onSettingsClick = {},
        onRefreshClick = {},
        isRefreshing = false
      )
    }
    val expectedTitle =
      InstrumentationRegistry.getInstrumentation().targetContext.getString(string.app_name)
    composeRule.onNodeWithTag(TAG_TITLE).assertTextContains(expectedTitle)
  }


  @Test
  fun doNotShowBackOnMain() {
    composeRule.setContent {
      ValiutchikTopBar(
        currentRoute = DESTINATION_MAIN,
        onBackClick = {},
        onAboutClick = {},
        onSettingsClick = {},
        onRefreshClick = {},
        isRefreshing = false
      )
    }

    composeRule.onNodeWithContentDescription(
      InstrumentationRegistry.getInstrumentation().targetContext.getString(
        string.topbar_description_back
      )
    ).assertDoesNotExist()
  }

  @Test
  fun showCorrectTitleForSettings() {
    composeRule.setContent {
      ValiutchikTopBar(
        currentRoute = DESTINATION_PREFERENCES,
        onBackClick = {},
        onAboutClick = {},
        onSettingsClick = {},
        onRefreshClick = {},
        isRefreshing = false
      )
    }

    val expectedTitle =
      InstrumentationRegistry.getInstrumentation().targetContext.getString(
        string.title_activity_settings
      )
    composeRule.onNodeWithTag(TAG_TITLE).assertTextContains(expectedTitle)
  }

  @Test
  fun changeStateOnNavigation() {
    composeRule.setContent {
      var route by remember {
        mutableStateOf(DESTINATION_MAIN)
      }
      ValiutchikTopBar(
        currentRoute = route,
        onBackClick = {},
        onAboutClick = {},
        onSettingsClick = { route = DESTINATION_PREFERENCES },
        onRefreshClick = {},
        isRefreshing = false
      )
    }

    val settingsDescription = InstrumentationRegistry.getInstrumentation().targetContext.getString(
      string.action_settings
    )
    composeRule.onNodeWithContentDescription(settingsDescription).performClick()
    val expectedTitle =
      InstrumentationRegistry.getInstrumentation().targetContext.getString(
        string.title_activity_settings
      )
    composeRule.onNodeWithTag(TAG_TITLE).assertTextContains(expectedTitle)
    composeRule.onNodeWithContentDescription(
      InstrumentationRegistry.getInstrumentation().targetContext.getString(
        string.topbar_description_back
      )
    ).assertIsDisplayed()
    composeRule.onNodeWithContentDescription(settingsDescription).assertDoesNotExist()
  }

  @Test
  fun showAboutDialog() {
    var isAboutDialogShown = false
    composeRule.setContent {
      ValiutchikTopBar(
        currentRoute = DESTINATION_MAIN,
        onBackClick = {},
        onAboutClick = { isAboutDialogShown = true },
        onSettingsClick = {},
        onRefreshClick = {},
        isRefreshing = false
      )
    }
    composeRule.onNodeWithContentDescription(
      InstrumentationRegistry.getInstrumentation().targetContext.getString(
        string.action_about
      )
    ).performClick()
    assertTrue(isAboutDialogShown)
  }

  @Test
  fun refreshWorks() {
    var isRefreshClicked = false
    composeRule.setContent {
      ValiutchikTopBar(
        currentRoute = DESTINATION_MAIN,
        onBackClick = {},
        onAboutClick = {},
        onSettingsClick = {},
        onRefreshClick = { isRefreshClicked = true },
        isRefreshing = false
      )
    }

    composeRule.onNodeWithContentDescription(
      InstrumentationRegistry.getInstrumentation().targetContext.getString(
        string.action_refresh
      )
    ).performClick()
    assertTrue(isRefreshClicked)
  }
}
