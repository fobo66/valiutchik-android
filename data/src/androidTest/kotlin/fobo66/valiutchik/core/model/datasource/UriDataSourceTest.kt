/*
 *    Copyright 2024 Andrey Mukamolov
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

package fobo66.valiutchik.core.model.datasource

import androidx.test.filters.SmallTest
import fobo66.valiutchik.core.model.repository.URI_AUTHORITY
import fobo66.valiutchik.core.model.repository.URI_PARAM_KEY
import fobo66.valiutchik.core.model.repository.URI_SCHEME
import org.junit.Assert.assertEquals
import org.junit.Test

@SmallTest
class UriDataSourceTest {

  private val uriDataSource: UriDataSource = UriDataSourceImpl()

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
      URI_SCHEME,
      URI_AUTHORITY,
      URI_PARAM_KEY,
      "test"
    )

    assertEquals("test", uri.getQueryParameter(URI_PARAM_KEY))
  }
}
