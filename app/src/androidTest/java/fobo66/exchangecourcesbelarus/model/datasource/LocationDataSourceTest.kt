package fobo66.exchangecourcesbelarus.model.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class LocationDataSourceTest {

  private val context = InstrumentationRegistry.getInstrumentation().targetContext
  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  private lateinit var locationDataSource: LocationDataSource

  @Before
  fun setUp() {
    locationDataSource = LocationDataSource(context, ioDispatcher, "test")
  }

  @Test
  fun resolveLocation() {
    runBlocking {
      val location = locationDataSource.resolveLocation()
      assertEquals(0.0, location.latitude, 0.0)
    }
  }
}
