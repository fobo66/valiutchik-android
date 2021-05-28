package fobo66.exchangecourcesbelarus.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import fobo66.valiutchik.core.UNKNOWN_COURSE
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

  @get:Rule
  var activityScenario = ActivityScenarioRule(MainActivity::class.java)

  @Test
  fun noDashesInCurrenciesValues() {
    MainScreen {
      coursesList {
        for (position in 0 until getSize()) {
          scrollTo(position)
          childAt<CoursesListItem>(position) {
            value.hasNoText(UNKNOWN_COURSE)
          }
        }
      }
    }
  }

  @Test
  fun showAboutDialog() {
    MainScreen {
      aboutIcon.click()
      aboutDialog.isDisplayed()
    }
  }

  @Test
  fun showSettings() {
    MainScreen {
      settingsIcon.click()
    }

    SettingsScreen {
      settings.isDisplayed()
    }
  }
}
