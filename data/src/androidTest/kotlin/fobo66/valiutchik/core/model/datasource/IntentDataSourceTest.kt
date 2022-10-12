package fobo66.valiutchik.core.model.datasource

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class IntentDataSourceTest {

  private val uri: Uri by lazy {
    Uri.parse("geo:0,0?q=test")
  }

  private lateinit var intentDataSource: IntentDataSource

  @Before
  fun setUp() {
    Intents.init()

    intentDataSource =
      IntentDataSourceImpl(InstrumentationRegistry.getInstrumentation().targetContext)
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun createIntent() {
    val intent = intentDataSource.createIntent(Uri.EMPTY)
    assertThat(intent, hasAction(Intent.ACTION_VIEW))
  }

  @Test
  fun canResolveIntent() {
    val intent = intentDataSource.createIntent(uri)
    assertNotNull("Can resolve intent", intentDataSource.resolveIntent(intent))
  }

  @Test
  fun cannotResolveEmptyIntent() {
    val intent = intentDataSource.createIntent(Uri.EMPTY)
    assertNull(intentDataSource.resolveIntent(intent))
  }
}
