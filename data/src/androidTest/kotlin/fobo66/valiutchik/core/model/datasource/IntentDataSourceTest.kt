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

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@SmallTest
class IntentDataSourceTest {

    @get:Rule
    val intentsRule = IntentsRule()

    private val intentDataSource: IntentDataSource =
        IntentDataSourceImpl(InstrumentationRegistry.getInstrumentation().targetContext)

    @Test
    fun createIntent() {
        val intent = intentDataSource.createIntent(Uri.EMPTY)
        assertThat(intent, hasAction(Intent.ACTION_VIEW))
    }

    @Test
    fun canResolveIntent() {
        val uri: Uri = Uri.parse("geo:0,0?q=test")
        val intent = intentDataSource.createIntent(uri)
        assertNotNull("Can resolve intent", intentDataSource.resolveIntent(intent))
    }

    @Test
    fun cannotResolveEmptyIntent() {
        val intent = intentDataSource.createIntent(Uri.EMPTY)
        assertNull(intentDataSource.resolveIntent(intent))
    }
}
