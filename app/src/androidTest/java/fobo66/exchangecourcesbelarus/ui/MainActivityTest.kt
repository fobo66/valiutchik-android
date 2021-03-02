package fobo66.exchangecourcesbelarus.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import fobo66.exchangecourcesbelarus.R
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
        children<CoursesListItem> {
          value.hasNoText(UNKNOWN_COURSE)
        }
      }
    }
  }

  @Test
  fun buySellIndicatorChanges() {
    MainScreen {
      buySellIndicator.hasText(R.string.sell)
      buySellSwitch.click()
      buySellIndicator.hasText(R.string.buy)
    }
  }

  @Test
  fun showAboutDialog() {
    MainScreen {
      aboutIcon.click()
      aboutDialog.isDisplayed()
    }
  }
}