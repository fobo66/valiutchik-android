package fobo66.exchangecourcesbelarus.ui.preferences

import app.cash.turbine.test
import fobo66.valiutchik.core.usecases.LoadDefaultCityPreference
import fobo66.valiutchik.core.usecases.LoadUpdateIntervalPreference
import fobo66.valiutchik.core.usecases.UpdateDefaultCityPreference
import fobo66.valiutchik.core.usecases.UpdateUpdateIntervalPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

  val defaultCity = MutableStateFlow("test")
  val updateInterval = MutableStateFlow(1f)

  private val loadUpdateIntervalPreference = object : LoadUpdateIntervalPreference {
    override fun execute(): Flow<Float> = updateInterval
  }

  private val loadDefaultCityPreference = object : LoadDefaultCityPreference {
    override fun execute(): Flow<String> = defaultCity
  }

  private val updateUpdateIntervalPreference = object : UpdateUpdateIntervalPreference {
    override suspend fun execute(newUpdateInterval: Float) {
      updateInterval.emit(newUpdateInterval)
    }
  }

  private val updateDefaultCityPreference = object : UpdateDefaultCityPreference {
    override suspend fun execute(newDefaultCity: String) {
      defaultCity.emit(newDefaultCity)
    }
  }

  @BeforeEach
  fun setUp() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    viewModel = PreferencesViewModel(
      loadDefaultCityPreference,
      loadUpdateIntervalPreference,
      updateDefaultCityPreference,
      updateUpdateIntervalPreference
    )
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `load default city pref`() = runTest {
    viewModel.defaultCityPreference.test {
      val awaitItem = awaitItem()
      assertEquals("test", awaitItem)
    }
  }

  @Test
  fun `update default city pref`() = runTest {
    viewModel.updateDefaultCity("newcity")

    viewModel.defaultCityPreference.test {
      val awaitItem = awaitItem()
      assertEquals("newcity", awaitItem)
    }
  }

  @Test
  fun `load update interval pref`() = runTest {
    viewModel.updateIntervalPreference.test {
      val awaitItem = awaitItem()
      assertEquals(1f, awaitItem)
    }
  }

  @Test
  fun `update update interval pref`() = runTest {
    viewModel.updateUpdateInterval(3f)

    viewModel.updateIntervalPreference.test {
      val awaitItem = awaitItem()
      assertEquals(3f, awaitItem)
    }
  }
}
