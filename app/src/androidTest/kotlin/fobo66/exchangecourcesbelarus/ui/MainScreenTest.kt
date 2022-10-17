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

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.filters.MediumTest
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.CurrenciesScreen.CoursesListItem
import fobo66.exchangecourcesbelarus.ui.main.MainScreen
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@MediumTest
class MainScreenTest : TestCase(
  kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun emptyList() = run {
    step("setup main screen") {
      composeRule.setContent {
        MainScreen(
          bestCurrencyRates = listOf(),
          isRefreshing = false,
          onRefresh = {},
          onBestRateClick = {},
          onBestRateLongClick = { _, _ -> }
        )
      }
    }
    step("check empty list indicator is visible") {
      ComposeScreen.onComposeScreen<CurrenciesScreen>(composeRule) {
        flakySafely {
          emptyListIndicator.assertIsDisplayed()
        }
      }
    }
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun openMap() = run {
    var isMapOpen = false
    step("setup main screen") {
      composeRule.setContent {
        MainScreen(
          bestCurrencyRates = listOf(BestCurrencyRate(0, "test", string.app_name, "0.0")),
          isRefreshing = false,
          onRefresh = {},
          onBestRateClick = {
            isMapOpen = true
          },
          onBestRateLongClick = { _, _ -> }
        )
      }
    }
    step("click on list item") {
      ComposeScreen.onComposeScreen<CurrenciesScreen>(composeRule) {
        flakySafely {
          coursesList.firstChild<CoursesListItem> {
            performClick()
          }
        }
      }
    }
    step("check map is opened") {
      assertTrue(isMapOpen)
    }
  }
}
