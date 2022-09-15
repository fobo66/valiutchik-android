package fobo66.exchangecourcesbelarus.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.main.ValiutchikTopBar
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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
          onSettingsClicked = {}
        )
      }
    }
    step("check title") {
      val expectedTitle =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(string.app_name)
      onComposeScreen<ToolbarScreen>(composeRule) {
        title.assert(hasText(expectedTitle))
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
          onSettingsClicked = {}
        )
      }
    }
    step("check back arrow") {
      onComposeScreen<ToolbarScreen>(composeRule) {
        backIcon.assertDoesNotExist()
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
          onSettingsClicked = {}
        )
      }
    }
    step("check title") {
      val expectedTitle =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(
          string.title_activity_settings
        )
      onComposeScreen<ToolbarScreen>(composeRule) {
        title.assert(hasText(expectedTitle))
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
          onSettingsClicked = { route = DESTINATION_PREFERENCES }
        )
      }
    }
    step("click on settings") {
      onComposeScreen<ToolbarScreen>(composeRule) {
        settingsIcon.performClick()
      }
    }
    step("check title") {
      val expectedTitle =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(
          string.title_activity_settings
        )
      onComposeScreen<ToolbarScreen>(composeRule) {
        title.assert(hasText(expectedTitle))
      }
    }
    step("check back icon") {
      onComposeScreen<ToolbarScreen>(composeRule) {
        backIcon.assertIsDisplayed()
      }
    }
    step("check settings icon") {
      onComposeScreen<ToolbarScreen>(composeRule) {
        settingsIcon.assertDoesNotExist()
      }
    }
  }
}
