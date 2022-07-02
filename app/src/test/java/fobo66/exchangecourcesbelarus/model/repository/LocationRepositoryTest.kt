package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.fake.FakeGeocodingDataSource
import fobo66.exchangecourcesbelarus.model.fake.FakeLocationDataSource
import fobo66.exchangecourcesbelarus.model.fake.FakePreferenceDataSource
import fobo66.valiutchik.core.model.repository.LocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 12/1/19.
 */
@ExperimentalCoroutinesApi
class LocationRepositoryTest {

  private lateinit var locationRepository: LocationRepository

  private val locationDataSource = FakeLocationDataSource()
  private val geocodingDataSource = FakeGeocodingDataSource()
  private val preferencesDataSource = FakePreferenceDataSource()

  @BeforeEach
  fun setUp() {
    locationRepository =
      LocationRepositoryImpl(locationDataSource, geocodingDataSource)
  }

  @AfterEach
  fun tearDown() {
    geocodingDataSource.reset()
    preferencesDataSource.reset()
  }

  @Test
  fun `resolve user city`() {
    runTest {
      val city = locationRepository.resolveUserCity()
      assertEquals("fake", city)
    }
  }

  @Test
  fun `return default city on HTTP error`() {
    geocodingDataSource.showError = true

    runTest {
      val city = locationRepository.resolveUserCity()
      assertEquals("default", city)
    }
  }

  @Test
  fun `crash on unexpected error`() {
    geocodingDataSource.unexpectedError = true

    runTest {
      assertThrows<KotlinNullPointerException> {
        locationRepository.resolveUserCity()
      }
    }
  }
}
