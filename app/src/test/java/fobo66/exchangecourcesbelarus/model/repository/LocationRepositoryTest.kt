package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.fake.FakeGeocodingDataSource
import fobo66.exchangecourcesbelarus.model.fake.FakeLocationDataSource
import fobo66.exchangecourcesbelarus.model.fake.FakePreferenceDataSource
import fobo66.valiutchik.core.model.repository.LocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

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

  @Before
  fun setUp() {
    locationRepository =
      LocationRepositoryImpl(locationDataSource, geocodingDataSource, preferencesDataSource)
  }

  @After
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

  @Test(expected = KotlinNullPointerException::class)
  fun `crash on unexpected error`() {
    geocodingDataSource.unexpectedError = true

    runTest {
      val city = locationRepository.resolveUserCity()
      assertEquals("default", city)
    }
  }
}
