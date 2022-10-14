/*
 *    Copyright 2022 Andrey Mukamolov
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

import android.Manifest
import android.content.Intent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import fobo66.exchangecourcesbelarus.ui.MainActivityScreen.CoursesListItem
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule
import org.junit.Test

@LargeTest
class MainActivityTest : TestCase(
  kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @get:Rule
  val grantPermissionsRule: GrantPermissionRule =
    GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun showMaps() = before {
    Intents.init()
  }.after {
    Intents.release()
  }.run {
    step("click on list item") {
      onComposeScreen<MainActivityScreen>(composeTestRule) {
        flakySafely {
          coursesList.firstChild<CoursesListItem> {
            performClick()
          }
        }
      }
    }
    step("check chooser is shown") {
      flakySafely {
        intended(hasAction(Intent.ACTION_CHOOSER))
      }
    }
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun copyToClipboard() = run {
    step("long press on list item") {
      onComposeScreen<MainActivityScreen>(composeTestRule) {
        flakySafely {
          coursesList.firstChild<CoursesListItem> {
            performGesture {
              longClick()
            }
          }
        }
      }
    }
    step("check snackbar is shown") {
      onComposeScreen<MainActivityScreen>(composeTestRule) {
        flakySafely {
          snackbar.assertIsDisplayed()
        }
      }
    }
  }

  @Test
  fun showSettings() = run {
    step("go to settings screen") {
      onComposeScreen<MainActivityScreen>(composeTestRule) {
        settingsIcon.performClick()
      }
    }

    step("check settings screen") {
      onComposeScreen<SettingsScreen>(composeTestRule) {
        flakySafely {
          settings.assertIsDisplayed()
        }
      }
    }
  }
}
