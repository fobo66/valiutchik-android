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
        children<CoursesListItem> {
          value.hasNoText(UNKNOWN_COURSE)
        }
      }
    }
  }
}