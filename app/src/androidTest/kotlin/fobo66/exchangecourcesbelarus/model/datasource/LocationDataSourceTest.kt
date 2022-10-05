package fobo66.exchangecourcesbelarus.model.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSourceImpl
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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
