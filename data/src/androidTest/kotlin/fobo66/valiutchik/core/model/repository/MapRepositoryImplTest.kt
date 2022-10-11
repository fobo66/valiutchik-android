package fobo66.valiutchik.core.model.repository

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSource
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MapRepositoryImplTest {

  private val uriDataSource = object : UriDataSource {
    override fun prepareUri(
      scheme: String,
      authority: String,
      queryParameterKey: String,
      queryParameterValue: String
    ): Uri = Uri.EMPTY
  }

  private val intentDataSource = object : IntentDataSource {
    var canResolveIntent = true

    override fun createIntent(uri: Uri, action: String): Intent = Intent()

    override fun resolveIntent(intent: Intent): ComponentName? = if (canResolveIntent) {
      InstrumentationRegistry.getInstrumentation().componentName
    } else {
      null
    }
  }

  private lateinit var mapRepository: MapRepository

  @Before
  fun setUp() {
    mapRepository = MapRepositoryImpl(uriDataSource, intentDataSource)
  }

  @After
  fun tearDown() {
    intentDataSource.canResolveIntent = true
  }

  @Test
  fun canResolveIntent() {
    assertNotNull(mapRepository.searchOnMap("test"))
  }

  @Test
  fun cannotResolveIntent() {
    intentDataSource.canResolveIntent = false

    assertNull(mapRepository.searchOnMap("test"))
  }
}
