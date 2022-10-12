package fobo66.valiutchik.core.model.datasource

import androidx.test.platform.app.InstrumentationRegistry
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocationDataSourceTest {

  private val context = InstrumentationRegistry.getInstrumentation().targetContext
  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  private lateinit var locationDataSource: LocationDataSource

  @Before
  fun setUp() {
    locationDataSource = LocationDataSourceImpl(context, ioDispatcher)
  }

  @Test
  fun resolveLocation() {
    runBlocking {
      val location = locationDataSource.resolveLocation()
      assertEquals(0.0, location.latitude, 0.0)
    }
  }
}
