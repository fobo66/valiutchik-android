package fobo66.exchangecourcesbelarus.ui.preferences

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import fobo66.exchangecourcesbelarus.entities.PreferenceScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesViewModelTest {
  private lateinit var viewModel: PreferencesViewModel

  @BeforeEach
  fun setUp() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    viewModel = PreferencesViewModel()
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `show loading state`() = runTest {
    viewModel.state.test {
        assertEquals(PreferenceScreenState.Loading, awaitItem())
    }
  }

  @Test
  fun `load preferences`() = runTest {
    viewModel.loadPreferences()
    viewModel.state.test {
        assertThat(awaitItem()).isInstanceOf(PreferenceScreenState.LoadedPreferences::class.java)
    }
  }
}
