package fobo66.exchangecourcesbelarus.ui.preferences

import app.cash.turbine.test
import fobo66.exchangecourcesbelarus.entities.PreferenceScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
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
  internal fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  internal fun `show loading state`() = runTest {
    viewModel.state.test {
        assertEquals(PreferenceScreenState.Loading, awaitItem())
    }
  }
}
