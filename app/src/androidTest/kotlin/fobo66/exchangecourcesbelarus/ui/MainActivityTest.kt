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

import android.Manifest
import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import dev.fobo66.valiutchik.ui.TAG_PREFERENCES
import dev.fobo66.valiutchik.ui.TAG_RATES
import dev.fobo66.valiutchik.ui.TAG_SNACKBAR
import fobo66.exchangecourcesbelarus.R
import org.junit.Rule
import org.junit.Test

@MediumTest
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val grantPermissionsRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    val intentsTestRule = IntentsRule()

    @Test
    fun showAboutDialog() {
        composeTestRule.onNodeWithContentDescription(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(
                R.string.action_about
            )
        ).performClick()
        composeTestRule.onNode(isDialog()).assertIsDisplayed()
    }

    @Test
    fun showMaps() {
        composeTestRule.onNodeWithTag(TAG_RATES)
            .onChildren()
            .onFirst()
            .performClick()

        intended(hasAction(Intent.ACTION_CHOOSER))
    }

    @Test
    fun copyToClipboard() {
        composeTestRule.onNodeWithTag(TAG_RATES)
            .onChildren()
            .onFirst()
            .performTouchInput {
                longClick()
            }

        composeTestRule.onNodeWithTag(TAG_SNACKBAR).assertIsDisplayed()
    }

    @Test
    fun showSettings() {
        composeTestRule.onNodeWithContentDescription(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(
                R.string.action_settings
            )
        ).performClick()
        composeTestRule.onNodeWithTag(TAG_PREFERENCES).assertIsDisplayed()
    }
}
