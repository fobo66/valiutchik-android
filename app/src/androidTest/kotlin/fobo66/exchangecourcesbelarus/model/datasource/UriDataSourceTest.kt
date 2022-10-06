package fobo66.exchangecourcesbelarus.model.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import fobo66.valiutchik.core.model.datasource.UriDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSourceImpl
import fobo66.valiutchik.core.model.repository.MapRepositoryImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UriDataSourceTest {

  private lateinit var uriDataSource: UriDataSource

  @Before
  fun setUp() {
    uriDataSource = UriDataSourceImpl()
  }

  @Test
  fun prepareHttpUri() {
    val uri = uriDataSource.prepareUri(
      "https",
      "example.com",
      "key",
      "value"
    )

    assertEquals("https://example.com?key=value", uri.toString())
  }

  @Test
  fun prepareMapUri() {
    val uri = uriDataSource.prepareUri(
      MapRepositoryImpl.URI_SCHEME,
      MapRepositoryImpl.URI_AUTHORITY,
      MapRepositoryImpl.URI_PARAM_KEY,
      "test"
    )

    assertEquals("test", uri.getQueryParameter(MapRepositoryImpl.URI_PARAM_KEY))
  }
}
