package fobo66.exchangecourcesbelarus.model.repository

import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.USER_CITY_KEY
import fobo66.valiutchik.core.entities.Location
import fobo66.valiutchik.core.model.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 12/1/19.
 */
class LocationRepositoryTest {

  private lateinit var locationRepository: LocationRepository

  private val locationDataSource: LocationDataSource = mockk {
    coEvery {
      resolveUserCity(any())
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

    coEvery {
      resolveLocation()
    } returns Location(0.0, 0.0)
  }

  private val preferencesDataSource: PreferencesDataSource = mockk {
    coEvery {
      saveString(USER_CITY_KEY, any())
    } returns Unit

    coEvery {
      loadString("default_city", "Минск")
    } returns "default"
  }

  @Before
  fun setUp() {
    locationRepository = LocationRepositoryImpl(locationDataSource, preferencesDataSource)
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

    coEvery {
      locationDataSource.resolveUserCity(any())
    } throws HttpException(
      Response.error<GeocodingResponse>(
        HttpsURLConnection.HTTP_INTERNAL_ERROR,
        "".toResponseBody()
      )
    )

    runBlocking {
      val city = locationRepository.resolveUserCity()
      assertEquals("default", city)
    }
  }

  @Test(expected = KotlinNullPointerException::class)
  fun `crash on unexpected error`() {

    coEvery {
      locationDataSource.resolveUserCity(any())
    } throws KotlinNullPointerException("test")

    runBlocking {
      val city = locationRepository.resolveUserCity()
      assertEquals("default", city)
    }
  }
}
