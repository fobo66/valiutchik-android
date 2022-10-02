package fobo66.exchangecourcesbelarus.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.swipeRight
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import fobo66.exchangecourcesbelarus.ui.preferences.PreferenceScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferencesScreenTest : TestCase(
  kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun showLicenses() = run {
    var showLicense = false
    step("setup screen") {
      composeRule.setContent {
        PreferenceScreen(
          defaultCityValue = "Minsk",
          updateIntervalValue = 1f,
          onDefaultCityChange = {},
          onUpdateIntervalChange = {},
          onOpenSourceLicensesClick = {
            showLicense = true
          }
        )
      }
    }
    step("click on licenses") {
      onComposeScreen<SettingsScreen>(composeRule) {
        licensesPreference.performClick()
      }
    }
    step("check if licenses are shown") {
      assertTrue(showLicense)
    }
  }

  @Test
  fun updateIntervalValueChanges() = run {
    var updateInterval = 1f
    step("setup screen") {
      composeRule.setContent {
        PreferenceScreen(
          defaultCityValue = "Minsk",
          updateIntervalValue = updateInterval,
          onDefaultCityChange = {},
          onUpdateIntervalChange = { updateInterval = it },
          onOpenSourceLicensesClick = {}
        )
      }
    }
    step("drag update interval slider") {
      onComposeScreen<SettingsScreen>(composeRule) {
        updateIntervalPreference.child<KNode> {
          hasTestTag("Slider")
        }.performGesture {
          swipeRight()
        }
      }
    }
    step("check value changed") {
      assertNotEquals(1f, updateInterval)
    }
  }

  @Test
  fun defaultCityDialogShown() = run {
    step("setup screen") {
      composeRule.setContent {
        PreferenceScreen(
          defaultCityValue = "Minsk",
          updateIntervalValue = 1f,
          onDefaultCityChange = {},
          onUpdateIntervalChange = {},
          onOpenSourceLicensesClick = {}
        )
      }
    }
    step("click on default city") {
      onComposeScreen<SettingsScreen>(composeRule) {
        defaultCityPreference.performClick()
      }
    }
    step("check dialog is shown") {
      onComposeScreen<SettingsScreen>(composeRule) {
        defaultCityPreferenceDialog.assertIsDisplayed()
      }
    }
  }
}
