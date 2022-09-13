package fobo66.exchangecourcesbelarus.ui

import android.Manifest
import android.content.Intent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import fobo66.exchangecourcesbelarus.ui.MainActivityScreen.CoursesListItem
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest : TestCase(
  kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @get:Rule
  val grantPermissionsRule: GrantPermissionRule =
    GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

  @Test
  fun showAboutDialog() = run {
    step("click on About icon") {
      onComposeScreen<MainActivityScreen>(composeTestRule) {
        aboutIcon.performClick()
      }
    }
    step("check if about info is displayed") {
      onComposeScreen<MainActivityScreen>(composeTestRule) {
        aboutDialog.assertIsDisplayed()
      }
    }
  }

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
