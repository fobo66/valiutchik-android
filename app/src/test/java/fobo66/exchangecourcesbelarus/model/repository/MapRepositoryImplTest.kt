package fobo66.exchangecourcesbelarus.model.repository

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import fobo66.exchangecourcesbelarus.model.datasource.UriDataSource
import fobo66.exchangecourcesbelarus.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.repository.MapRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MapRepositoryImplTest {

  private val uri: Uri = mockk()
  private val intent: Intent = mockk()
  private val componentName: ComponentName = mockk()

  private val uriDataSource: UriDataSource = mockk {
    every { prepareUri(any(), any(), any(), any()) } returns uri
  }

  private val intentDataSource: IntentDataSource = mockk {
    every { createIntent(any(), any()) } returns intent
  }

  private lateinit var mapRepository: MapRepository

  @Before
  fun setUp() {
    mapRepository = MapRepositoryImpl(uriDataSource, intentDataSource)
  }

  @Test
  fun `can resolve intent`() {
    every {
      intentDataSource.resolveIntent(any())
    } returns componentName

    assertNotNull(mapRepository.searchOnMap("test"))
  }

  @Test
  fun `cannot resolve intent`() {
    every {
      intentDataSource.resolveIntent(any())
    } returns null

    assertNull(mapRepository.searchOnMap("test"))
  }
}
