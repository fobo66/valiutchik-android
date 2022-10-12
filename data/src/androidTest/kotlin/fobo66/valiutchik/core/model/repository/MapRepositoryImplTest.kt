/*
 *    Copyright 2022 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
