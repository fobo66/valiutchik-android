package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.fake.FakeGeocodingDataSource
import fobo66.valiutchik.core.fake.FakeLocationDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 12/1/19.
 */
@ExperimentalCoroutinesApi
class LocationRepositoryTest {

  private val locationDataSource = FakeLocationDataSource()
  private val geocodingDataSource = FakeGeocodingDataSource()

  private val locationRepository: LocationRepository =
    LocationRepositoryImpl(locationDataSource, geocodingDataSource)

  @Test
  fun `resolve user city`() = runTest {
    val city = locationRepository.resolveUserCity("default")
    assertEquals("fake", city)
  }

  @Test
  fun `return default city on HTTP error`() {
    geocodingDataSource.showError = true

    runTest {
      val city = locationRepository.resolveUserCity("default")
      assertEquals("default", city)
    }
  }

  @Test
  fun `crash on unexpected error`() {
    geocodingDataSource.unexpectedError = true

    runTest {
      assertThrows<KotlinNullPointerException> {
        locationRepository.resolveUserCity("default")
      }
    }
  }
}
