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
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.ext.truth.content.IntentSubject.assertThat as assertIntent
import androidx.test.filters.SmallTest
import com.eygraber.uri.Uri
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

@SmallTest
class IntentDataSourceTest {
    private val uri: Uri = Uri.parse("geo:0,0?q=test")

    @get:Rule
    val intentsRule = IntentsRule()

    private val intentDataSource: IntentDataSource =
        IntentDataSourceImpl(ApplicationProvider.getApplicationContext())

    @Test
    fun createIntent() {
        val intent = Intent.parseUri(intentDataSource.createIntentUri(Uri.EMPTY), 0)
        assertIntent(intent).hasAction(Intent.ACTION_VIEW)
    }

    @Test
    fun canResolveIntent() {
        val intent = intentDataSource.createIntentUri(uri)
        assertThat(intentDataSource.checkIntentUri(intent)).isTrue()
    }

    @Test
    fun cannotResolveEmptyIntent() {
        val intent = intentDataSource.createIntentUri(Uri.EMPTY)
        assertThat(intentDataSource.checkIntentUri(intent)).isFalse()
    }
}
