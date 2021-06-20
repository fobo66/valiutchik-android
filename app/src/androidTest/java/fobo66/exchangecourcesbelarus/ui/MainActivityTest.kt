package fobo66.exchangecourcesbelarus.ui

import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.rules.ActivityScenarioRule
import fobo66.valiutchik.core.UNKNOWN_COURSE
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

  @get:Rule
  var activityScenario = ActivityScenarioRule(MainActivity::class.java)

  @Before
  fun setUp() {
    Intents.init()
  }

  @After
  fun tearDown() {
    Intents.release()
  }

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
  fun showMaps() {
    MainScreen {
      coursesList.firstChild<CoursesListItem> {
        click()
      }
    }
    intended(hasAction(Intent.ACTION_CHOOSER))
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
