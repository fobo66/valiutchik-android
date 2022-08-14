package fobo66.exchangecourcesbelarus.ui

import android.Manifest
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.testcases.api.testcaserule.TestCaseRule
import fobo66.valiutchik.core.UNKNOWN_COURSE
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

  @get:Rule
  val activityScenario = ActivityScenarioRule(MainActivity::class.java)

  @get:Rule
  val grantPermissionsRule: GrantPermissionRule =
    GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

  @get:Rule
  val testCaseRule = TestCaseRule(javaClass.simpleName)

  @Test
  fun noDashesInCurrenciesValues() = testCaseRule.run {
    step("check for no dashes in list") {
      MainScreen.coursesList {
        flakySafely {
          for (position in 0 until getSize()) {
            scrollTo(position)
            childAt<CoursesListItem>(position) {
              value.hasNoText(UNKNOWN_COURSE)
            }
          }
        }
      }
    }
  }

  @Test
  fun showAboutDialog() = testCaseRule.run {
    step("click on About icon") {
      MainScreen.aboutIcon.click()
    }
    step("check if about info is displayed") {
      MainScreen.aboutDialog.isDisplayed()
    }
  }

  @Test
  fun showMaps() = testCaseRule
    .before {
      Intents.init()
    }.after {
      Intents.release()
    }.run {
      step("click on list item") {
        MainScreen {
          flakySafely {
            coursesList.firstChild<CoursesListItem> {
              click()
            }
          }
        }
      }
      step("check chooser is shown") {
        intended(hasAction(Intent.ACTION_CHOOSER))
      }
    }

  @Test
  fun copyToClipboard() = testCaseRule.run {
    step("long press on list item") {
      MainScreen {
        flakySafely {
          coursesList.firstChild<CoursesListItem> {
            longClick()
          }
        }
      }
    }
    step("check snackbar is shown") {
      MainScreen {
        flakySafely {
          snackbar.isDisplayed()
        }
      }
    }
  }

  @Test
  fun showSettings() = testCaseRule.run {
    step("open settings") {
      MainScreen {
        settingsIcon.click()
      }
    }

    step("check settings screen") {
      SettingsScreen {
        flakySafely {
          settings.isDisplayed()
        }
      }
    }
  }
}
