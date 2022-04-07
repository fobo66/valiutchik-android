package fobo66.exchangecourcesbelarus.model.repository

import com.mapbox.search.result.SearchAddress
import com.mapbox.search.result.SearchResult
import fobo66.exchangecourcesbelarus.model.datasource.GeocodingDataSource
import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.entities.Location
import fobo66.valiutchik.core.model.repository.LocationRepository
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 12/1/19.
 */
class LocationRepositoryTest {

  private lateinit var locationRepository: LocationRepository

  private val searchAddress: SearchAddress = mockk {
    every {
      locality
    } returns "test"
  }

  private val searchResult: SearchResult = mockk {
    every {
      address
    } returns searchAddress
  }

  private val locationDataSource = object : LocationDataSource {
    override suspend fun resolveLocation(): Location =
      Location(0.0, 0.0)
  }

  private val geocodingDataSource = object : GeocodingDataSource {
    var showError = false
    var unexpectedError = false

    override suspend fun resolveUserCity(location: Location): List<SearchResult> =
      when {
        showError -> throw IOException("Yikes!")
        unexpectedError -> throw KotlinNullPointerException("Yikes!")
        else -> listOf(searchResult)
      }
  }

  private val preferencesDataSource = object : PreferencesDataSource {
    override fun loadString(key: String, defaultValue: String): String =
      "default"

    override fun saveString(key: String, value: String) = Unit

    override fun loadInt(key: String, defaultValue: Int): Int = 0

    override fun saveInt(key: String, value: Int) = Unit
  }

  @Before
  fun setUp() {
    locationRepository =
      LocationRepositoryImpl(locationDataSource, geocodingDataSource, preferencesDataSource)
  }

  @After
  fun tearDown() {
    geocodingDataSource.apply {
      showError = false
      unexpectedError = false
    }
  }

  @Test
  fun `resolve user city`() {

    runBlocking {
      val city = locationRepository.resolveUserCity()
      assertEquals("test", city)
    }
  }

  @Test
  fun `return default city on HTTP error`() {
    geocodingDataSource.showError = true

    runBlocking {
      val city = locationRepository.resolveUserCity()
      assertEquals("default", city)
    }
  }

  @Test(expected = KotlinNullPointerException::class)
  fun `crash on unexpected error`() {
    geocodingDataSource.unexpectedError = true

    runBlocking {
      val city = locationRepository.resolveUserCity()
      assertEquals("default", city)
    }
  }
}
