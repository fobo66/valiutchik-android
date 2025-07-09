/*
 *    Copyright 2025 Andrey Mukamolov
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

import kotlin.test.Test
import kotlin.test.assertEquals

class UriDataSourceJvmImplTest {
    private val uriDataSource = UriDataSourceJvmImpl()

    @Test
    fun `prepare HTTP URI`() {
        val uri =
            uriDataSource.prepareUri("test")

        assertEquals(URI_AUTHORITY, uri.authority)
    }
}
