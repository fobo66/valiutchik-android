package fobo66.exchangecourcesbelarus.ui.preferences

import app.cash.turbine.test
import fobo66.valiutchik.core.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.core.usecases.LoadUpdateIntervalPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesViewModelTest {
  private lateinit var viewModel: PreferencesViewModel

  private val loadUpdateIntervalPreference = object : LoadUpdateIntervalPreference {
    override fun execute(): Flow<Float> = flowOf(1f)
  }

  private val loadDefaultCityPreference = object : LoadDefaultCityPreference {
    override fun execute(): Flow<String> = flowOf("test")
  }

  @BeforeEach
  fun setUp() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    viewModel = PreferencesViewModel(loadDefaultCityPreference, loadUpdateIntervalPreference)
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `load default city pref`() = runTest {
    viewModel.defaultCityPreference.test {
      val awaitItem = awaitItem()
      awaitComplete()
      assertEquals("test", awaitItem)
    }
  }

  @Test
  fun `load update interval pref`() = runTest {
    viewModel.updateIntervalPreference.test {
      val awaitItem = awaitItem()
      awaitComplete()
      assertEquals(1f, awaitItem)
    }
  }
}
