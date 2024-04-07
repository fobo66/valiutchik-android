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
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.main.ValiutchikTopBar
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@MediumTest
class ToolbarTest : TestCase(
  kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun showCorrectTitle() = run {
    step("setup title") {
      composeRule.setContent {
        ValiutchikTopBar(
          currentRoute = DESTINATION_MAIN,
          onBackClick = {},
          onAboutClick = {},
          onSettingsClicked = {},
          onRefreshClicked = {},
          isRefreshing = false
        )
      }
    }
    step("check title") {
      val expectedTitle =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(string.app_name)
      onComposeScreen<ToolbarScreen>(composeRule) {
        flakySafely {
          title.assert(hasText(expectedTitle))
        }
      }
    }
  }

  @Test
  fun doNotShowBackOnMain() = run {
    step("setup title") {
      composeRule.setContent {
        ValiutchikTopBar(
          currentRoute = DESTINATION_MAIN,
          onBackClick = {},
          onAboutClick = {},
          onSettingsClicked = {},
          onRefreshClicked = {},
          isRefreshing = false
        )
      }
    }
    step("check back arrow") {
      onComposeScreen<ToolbarScreen>(composeRule) {
        flakySafely {
          backIcon.assertDoesNotExist()
        }
      }
    }
  }

  @Test
  fun showCorrectTitleForSettings() = run {
    step("setup title") {
      composeRule.setContent {
        ValiutchikTopBar(
          currentRoute = DESTINATION_PREFERENCES,
          onBackClick = {},
          onAboutClick = {},
          onSettingsClicked = {},
          onRefreshClicked = {},
          isRefreshing = false
        )
      }
    }
    step("check title") {
      val expectedTitle =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(
          string.title_activity_settings
        )
      flakySafely {
        onComposeScreen<ToolbarScreen>(composeRule) {
          title.assert(hasText(expectedTitle))
        }
      }
    }
  }

  @Test
  fun changeStateOnNavigation() = run {
    step("setup title") {
      composeRule.setContent {
        var route by remember {
          mutableStateOf(DESTINATION_MAIN)
        }
        ValiutchikTopBar(
          currentRoute = route,
          onBackClick = {},
          onAboutClick = {},
          onSettingsClicked = { route = DESTINATION_PREFERENCES },
          onRefreshClicked = {},
          isRefreshing = false
        )
      }
    }
    step("click on settings") {
      flakySafely {
        onComposeScreen<ToolbarScreen>(composeRule) {
          settingsIcon.performClick()
        }
      }
    }
    step("check title") {
      val expectedTitle =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(
          string.title_activity_settings
        )
      flakySafely {
        onComposeScreen<ToolbarScreen>(composeRule) {
          title.assert(hasText(expectedTitle))
        }
      }
    }
    step("check back icon") {
      flakySafely {
        onComposeScreen<ToolbarScreen>(composeRule) {
          backIcon.assertIsDisplayed()
        }
      }
    }
    step("check settings icon") {
      flakySafely {
        onComposeScreen<ToolbarScreen>(composeRule) {
          settingsIcon.assertDoesNotExist()
        }
      }
    }
  }

  @Test
  fun showAboutDialog() = run {
    var isAboutDialogShown = false
    step("setup about icon") {
      composeRule.setContent {
        ValiutchikTopBar(
          currentRoute = DESTINATION_MAIN,
          onBackClick = {},
          onAboutClick = { isAboutDialogShown = true },
          onSettingsClicked = {},
          onRefreshClicked = {},
          isRefreshing = false
        )
      }
    }
    step("click about icon") {
      flakySafely {
        onComposeScreen<ToolbarScreen>(composeRule) {
          aboutIcon.performClick()
        }
      }
    }
    step("check dialog") {
      assertTrue(isAboutDialogShown)
    }
  }

  @Test
  fun refreshWorks() = run {
    var isRefreshClicked = false
    step("setup refresh icon") {
      composeRule.setContent {
        ValiutchikTopBar(
          currentRoute = DESTINATION_MAIN,
          onBackClick = {},
          onAboutClick = {},
          onSettingsClicked = {},
          onRefreshClicked = { isRefreshClicked = true },
          isRefreshing = false
        )
      }
    }
    step("click about icon") {
      flakySafely {
        onComposeScreen<ToolbarScreen>(composeRule) {
          refreshIcon.performClick()
        }
      }
    }
    step("check dialog") {
      assertTrue(isRefreshClicked)
    }
  }
}
