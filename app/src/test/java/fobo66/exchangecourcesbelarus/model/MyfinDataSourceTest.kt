package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.MyfinDataSource
import fobo66.valiutchik.core.BASE_URL
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.junit4.MockWebServerRule
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.net.ssl.HttpsURLConnection

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
class MyfinDataSourceTest {

  @JvmField
  @Rule
  val serverRule = MockWebServerRule()

  private lateinit var dataSource: CurrencyRatesDataSource

  @Before
  fun setUp() {
    dataSource = MyfinDataSource(
      OkHttpClient(),
      serverRule.server.url("").toString()
    )
  }

  @Test
  fun `load exchange rates`() {
    serverRule.server.enqueue(MockResponse())

    runBlocking {
      val response = dataSource.loadExchangeRates("Минск")
      assertTrue(response.isSuccessful)
    }
  }

  @Test
  fun `correct url for city`() {
    serverRule.server.enqueue(MockResponse())

    runBlocking {
      val response = dataSource.loadExchangeRates("Могилёв")
      assertEquals("/6", response.request.url.encodedPath)
    }
  }

  @Test
  fun `default url for unknown city`() {
    serverRule.server.enqueue(MockResponse())

    runBlocking {
      val response = dataSource.loadExchangeRates("test")
      assertEquals("/1", response.request.url.encodedPath)
    }
  }

  @Test
  fun `error while loading exchange rates`() {
    serverRule.server.enqueue(MockResponse().setResponseCode(HttpsURLConnection.HTTP_INTERNAL_ERROR))

    runBlocking {
      val response = dataSource.loadExchangeRates("Минск")
      assertFalse(response.isSuccessful)
    }
  }

  @Test
  fun `myfin url processing is correct`() {
    val myfinUrl = BASE_URL.toHttpUrl().newBuilder()
      .addPathSegment("1")
      .build()
    assertEquals(
      "/outer/authXml/1",
      myfinUrl
        .encodedPath
    )
  }
}
