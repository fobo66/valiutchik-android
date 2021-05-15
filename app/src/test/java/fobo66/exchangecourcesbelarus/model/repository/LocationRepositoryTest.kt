package fobo66.exchangecourcesbelarus.model.repository

import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.USER_CITY_KEY
import fobo66.valiutchik.core.model.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 12/1/19.
 */
class LocationRepositoryTest {

  private lateinit var locationRepository: LocationRepository

  private lateinit var locationDataSource: LocationDataSource
  private lateinit var preferencesDataSource: PreferencesDataSource

  @Before
  fun setUp() {
    locationDataSource = mockk()
    preferencesDataSource = mockk()

    coEvery {
      locationDataSource.resolveUserCity(any())
    } returns GeocodingResponse.fromJson(
      """
      {
        "type": "Feature",
        "query": ["aaa"],
        "features": [{
          "type": "place",
          "text": "test"
        }],
        "attribution": "aaa",
        "geometry": {
          "type": "Point",
          "coordinates": [125.6, 10.1]
        },
        "properties": {
          "name": "Dinagat Islands"
        }
      }
      """.trimIndent()
    )

    every {
      preferencesDataSource.saveString(USER_CITY_KEY, any())
    } returns Unit

    every {
      preferencesDataSource.loadSting("default_city", "Минск")
    } returns "default"

    locationRepository = LocationRepositoryImpl(locationDataSource, preferencesDataSource)
  }

  @Test
  fun `resolve user city`() {

    runBlocking {
      val city = locationRepository.resolveUserCity(0.0, 0.0)
      assertEquals("test", city)
    }
  }

  @Test
  fun `return default city on error`() {

    coEvery {
      locationDataSource.resolveUserCity(any())
    } throws Exception("test")

    runBlocking {
      val city = locationRepository.resolveUserCity(0.0, 0.0)
      assertEquals("default", city)
    }
  }
}