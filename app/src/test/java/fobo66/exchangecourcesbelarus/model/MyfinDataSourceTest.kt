package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.util.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.net.ssl.HttpsURLConnection

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
class MyfinDataSourceTest {

  private lateinit var mockWebServer: MockWebServer
  private lateinit var myfinDataSource: MyfinDataSource

  @Before
  fun setUp() {
    mockWebServer = MockWebServer()
    myfinDataSource = MyfinDataSource(OkHttpClient(), mockWebServer.url("").toString())
  }

  @After
  fun tearDown() {
    mockWebServer.close()
  }

  @Test
  fun loadExchangeRates() {
    mockWebServer.enqueue(MockResponse())

    runBlocking {
      val response = myfinDataSource.loadExchangeRates("Минск")
      assertTrue(response.isSuccessful)
    }

  }

  @Test
  fun loadExchangeRates_correctCityUrl() {
    mockWebServer.enqueue(MockResponse())

    runBlocking {
      val response = myfinDataSource.loadExchangeRates("Могилёв")
      assertEquals("/6", response.request.url.encodedPath)
    }

  }

  @Test
  fun loadExchangeRates_defaultCityWhenCityIsNotChecked() {
    mockWebServer.enqueue(MockResponse())

    runBlocking {
      val response = myfinDataSource.loadExchangeRates("test")
      assertEquals("/1", response.request.url.encodedPath)
    }
  }

  @Test
  fun loadExchangeRates_error() {
    mockWebServer.enqueue(MockResponse().setResponseCode(HttpsURLConnection.HTTP_INTERNAL_ERROR))

    runBlocking {
      val response = myfinDataSource.loadExchangeRates("Минск")
      assertFalse(response.isSuccessful)
    }
  }

  @Test
  fun myfinUrlProcessing() {
    val myfinUrl = Constants.BASE_URL.toHttpUrl().newBuilder()
      .addPathSegment("1")
      .build()
    assertEquals(
      "/outer/authXml/1", myfinUrl
        .encodedPath
    )

  }
}