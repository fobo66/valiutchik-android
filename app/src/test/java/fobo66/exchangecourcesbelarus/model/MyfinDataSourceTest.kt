package fobo66.exchangecourcesbelarus.model

import kotlinx.coroutines.runBlocking
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
    myfinDataSource = MyfinDataSource(OkHttpClient(), mockWebServer.url("/%d").toString())
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
}